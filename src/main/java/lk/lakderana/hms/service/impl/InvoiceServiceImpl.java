package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.*;
import lk.lakderana.hms.entity.*;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.InquiryMapper;
import lk.lakderana.hms.mapper.InvoiceMapper;
import lk.lakderana.hms.repository.*;
import lk.lakderana.hms.service.InvoiceService;
import lk.lakderana.hms.service.PartyService;
import lk.lakderana.hms.service.PaymentService;
import lk.lakderana.hms.service.ReservationService;
import lk.lakderana.hms.util.DateConversion;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.assertj.core.util.Strings;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class InvoiceServiceImpl extends EntityValidator implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetRepository invoiceDetRepository;
    private final ReservationRepository reservationRepository;
    private final NumberGeneratorRepository numberGeneratorRepository;
    private final BranchRepository branchRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final FacilityReservationRepository facilityReservationRepository;
    private final ItemReservationRepository itemReservationRepository;

    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final PartyService partyService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceDetRepository invoiceDetRepository,
                              ReservationRepository reservationRepository,
                              NumberGeneratorRepository numberGeneratorRepository,
                              BranchRepository branchRepository,
                              RoomReservationRepository roomReservationRepository,
                              FacilityReservationRepository facilityReservationRepository,
                              ItemReservationRepository itemReservationRepository,
                              PaymentService paymentService,
                              @Lazy ReservationService reservationService, 
                              PartyService partyService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetRepository = invoiceDetRepository;
        this.reservationRepository = reservationRepository;
        this.numberGeneratorRepository = numberGeneratorRepository;
        this.branchRepository = branchRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.facilityReservationRepository = facilityReservationRepository;
        this.itemReservationRepository = itemReservationRepository;
        this.paymentService = paymentService;
        this.reservationService = reservationService;
        this.partyService = partyService;
    }

    @Transactional
    @Override
    public InvoiceDTO createInvoice(Long reservationId, InvoiceDTO invoiceDTO) {

        validateEntity(invoiceDTO);
        final TMsReservation tMsReservation = validateReservationById(reservationId);

        final BigDecimal dueAmount = paymentService.calculateDueAmountForAReservation(reservationId, true);

        if(dueAmount.compareTo(BigDecimal.ZERO) != 0)
            throw new OperationException("Settle Dues before creating the Invoice. Due Amount : " + dueAmount);

        final String invoiceNumber = numberGeneratorRepository.generateNumber("IV", "Y", "#", "#",
                "#", "#", "#", "#");

        if(Strings.isNullOrEmpty(invoiceNumber))
            throw new OperationException("Invoice Number not generated successfully");

        invoiceDTO.setInvoiceNumber(invoiceNumber);
        invoiceDTO.setStatus(STATUS_ACTIVE.getShortValue());
        invoiceDTO.setBranchId(captureBranchIds().get(0));
        invoiceDTO.setGrossAmount(reservationService.calculateTotalReservationAmount(reservationId));
        invoiceDTO.setReservationId(reservationId);

        calculateInvoiceAmounts(invoiceDTO);

        final TTrInvoice createdInvoice = persistEntity(InvoiceMapper.INSTANCE.dtoToEntity(invoiceDTO));

        insertToInvoiceDet(reservationId, createdInvoice);

        return InvoiceMapper.INSTANCE.entityToDTO(createdInvoice);
    }

    @Override
    public InvoicePrintDTO getInvoiceDataByReservation(Long reservationId) {

        InvoicePrintDTO invoicePrintDTO = new InvoicePrintDTO();

        final TMsReservation tMsReservation = validateReservationById(reservationId);

        final TTrInvoice tTrInvoice = invoiceRepository
                .findByReservation_ResvIdAndAndBranch_BrnhIdInAndInvcStatus(reservationId, captureBranchIds(), STATUS_ACTIVE.getShortValue());

        InvoiceDTO invoiceDTO = InvoiceMapper.INSTANCE.entityToDTO(tTrInvoice);

        final URL logoImage = this.getClass().getClassLoader().getResource("reports/images/lakderana_logo.png");

        invoicePrintDTO.setLogoImage(logoImage);

        invoicePrintDTO.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoicePrintDTO.setCreatedDate(DateConversion.convertLocalDateTimeToString(invoiceDTO.getCreatedDate()));
        invoicePrintDTO.setBranchName(invoiceDTO.getBranchName());
        invoicePrintDTO.setCheckInDateTime(DateConversion.convertLocalDateTimeToString(tMsReservation.getResvCheckInDateTime()));
        invoicePrintDTO.setCheckOutDateTime(DateConversion.convertLocalDateTimeToString(tMsReservation.getResvCheckOutDateTime()));
        invoicePrintDTO.setNoOfAdults(tMsReservation.getResvNoOfAdults() == null ? "0" : tMsReservation.getResvNoOfAdults().toString());
        invoicePrintDTO.setNoOfChildren(tMsReservation.getResvNoOfChildren() == null ? "0" : tMsReservation.getResvNoOfChildren().toString());

        final long daysBetween = Duration.between(tMsReservation.getResvCheckInDateTime(), tMsReservation.getResvCheckOutDateTime()).toDays();
        invoicePrintDTO.setNoOfDays(String.valueOf(daysBetween));

        PartyDTO partyDTO = null;
        if(!Strings.isNullOrEmpty(tMsReservation.getInquiry().getInqrCustomerCode())) {
            partyDTO = partyService.getPartyByPartyCode(tMsReservation.getInquiry().getInqrCustomerCode());
            invoicePrintDTO.setCustomerName(partyDTO.getName());
        }
        else
            invoicePrintDTO.setCustomerName(tMsReservation.getInquiry().getInqrCustomerName());

        final List<String> cnmbl = partyDTO.getContactList()
                .stream()
                .filter(partyContactDTO -> partyContactDTO.getContactType().equals("CNMBL"))
                .map(partyContactDTO -> partyContactDTO.getContactNumber())
                .collect(Collectors.toList());
        if(!cnmbl.isEmpty())
            invoicePrintDTO.setCustomerContactNo(cnmbl.get(0));
        else
            invoicePrintDTO.setCustomerContactNo(tMsReservation.getInquiry().getInqrCustomerContactNo());

        if(partyDTO != null) {
            invoicePrintDTO.setCustomerAddress(partyDTO.getAddress1() + ", " + partyDTO.getAddress2() + ", " + partyDTO.getAddress3());
            invoicePrintDTO.setCustomerCode(partyDTO.getPartyCode());
        } else {
            invoicePrintDTO.setCustomerAddress("-");
            invoicePrintDTO.setCustomerCode("-");
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

        invoicePrintDTO.setGrossAmount(decimalFormat.format(invoiceDTO.getGrossAmount()));
        invoicePrintDTO.setTaxAmount(decimalFormat.format(invoiceDTO.getTaxAmount()));
        invoicePrintDTO.setDiscountAmount(decimalFormat.format(invoiceDTO.getDiscountAmount()));
        invoicePrintDTO.setNetAmount(decimalFormat.format(invoiceDTO.getNetAmount()));

        List<InvoicePrintReservationInfoDTO> printReservationInfoDTOList = new ArrayList<>();

        final List<TTrInvoiceDet> tTrInvoiceDetList = invoiceDetRepository
                .findAllByReservation_ResvIdAndAndBranch_BrnhIdIn(reservationId, captureBranchIds());

        tTrInvoiceDetList.forEach(tTrInvoiceDet -> {

            InvoicePrintReservationInfoDTO invoicePrintReservationInfoDTO = new InvoicePrintReservationInfoDTO();

            final TTrFacilityReservation facilityReservation = tTrInvoiceDet.getFacilityReservation();

            if(facilityReservation != null) {
                invoicePrintReservationInfoDTO.setItem(facilityReservation.getFacility().getFcltName());
                invoicePrintReservationInfoDTO.setQuantity(facilityReservation.getFareQuantity().toString());
                invoicePrintReservationInfoDTO.setUnitPrice(decimalFormat.format(facilityReservation.getFacility().getFcltPrice()));
                invoicePrintReservationInfoDTO.setAmount(decimalFormat.format(facilityReservation.getFacility().getFcltPrice()
                        .multiply(BigDecimal.valueOf(facilityReservation.getFareQuantity()))));
                invoicePrintReservationInfoDTO.setItemReservedDateTime(DateConversion.convertDateToStringWithTime(facilityReservation.getCreatedDate()));

                printReservationInfoDTOList.add(invoicePrintReservationInfoDTO);
            }

            final TTrRoomReservation roomReservation = tTrInvoiceDet.getRoomReservation();

            if(roomReservation != null) {
                invoicePrintReservationInfoDTO.setItem(roomReservation.getRoom().getRoomNo());
                invoicePrintReservationInfoDTO.setQuantity("1");
                invoicePrintReservationInfoDTO.setUnitPrice(decimalFormat.format(roomReservation.getRoom().getRoomPrice()));
                invoicePrintReservationInfoDTO.setAmount(decimalFormat.format(roomReservation.getRoom().getRoomPrice()
                        .multiply(BigDecimal.valueOf(1))));
                invoicePrintReservationInfoDTO.setItemReservedDateTime(DateConversion.convertDateToStringWithTime(roomReservation.getCreatedDate()));

                printReservationInfoDTOList.add(invoicePrintReservationInfoDTO);
            }

            final TTrItemReservation itemReservation = tTrInvoiceDet.getItemReservation();

            if(itemReservation != null) {
                invoicePrintReservationInfoDTO.setItem(itemReservation.getItem().getItemName());
                invoicePrintReservationInfoDTO.setQuantity(itemReservation.getItrsQuantity().toString());
                invoicePrintReservationInfoDTO.setUnitPrice(decimalFormat.format(itemReservation.getItem().getItemPrice()));
                invoicePrintReservationInfoDTO.setAmount(decimalFormat.format(itemReservation.getItem().getItemPrice()
                        .multiply(BigDecimal.valueOf(itemReservation.getItrsQuantity()))));
                invoicePrintReservationInfoDTO.setItemReservedDateTime(DateConversion.convertDateToStringWithTime(itemReservation.getCreatedDate()));

                printReservationInfoDTOList.add(invoicePrintReservationInfoDTO);
            }
        });

        invoicePrintDTO.setReservationDetailsDataSource(new JRBeanCollectionDataSource(printReservationInfoDTOList));

        return invoicePrintDTO;
    }

    @Override
    public JasperPrint generateInvoicePrint(Long reservationId) throws IOException {
        InputStream input = null;
        try {
            input = Objects.requireNonNull(this.getClass().getClassLoader().getResource("reports/invoice/invoice.jrxml")).openStream();

            InvoicePrintDTO invoicePrintDTO = getInvoiceDataByReservation(reservationId);

            JasperDesign design = null;
            try {
                design = JRXmlLoader.load(input);
            } catch (JRException e) {
                throw new OperationException("Cannot Load the Jasper Template.");
            }
            JasperReport report = null;
            try {
                report = JasperCompileManager.compileReport(design);
            } catch (JRException e) {
                throw new OperationException("Cannot Compile the Jasper Template.");
            }
            JRBeanArrayDataSource beanColDataSource = new JRBeanArrayDataSource(new InvoicePrintDTO[]{invoicePrintDTO});
            JasperPrint print = null;
            try {
                print = JasperFillManager.fillReport(report, new HashMap(), beanColDataSource);
            } catch (JRException e) {
                log.error("" + e);
                throw new OperationException("Cannot Fill the Jasper Template.");
            }
            removeBlankPage(print.getPages());
            return print;

        } catch (IOException e) {
            throw new OperationException("Cannot Find the Jasper Template.");
        } finally {
            if (input != null)
                input.close();
        }
    }

    @Override
    public PaginatedEntity invoicePaginatedSearch(Long reservationId, String invoiceNumber, Short status, Integer page, Integer size) {

        PaginatedEntity paginatedInvoiceList = null;
        List<InvoiceDTO> invoiceDTOList = null;

        validatePaginateIndexes(page, size);

        final Page<TTrInvoice> tTrInvoicePage = invoiceRepository
                .getActiveInvoice(reservationId, invoiceNumber, status, captureBranchIds(), PageRequest.of(page - 1, size));

        if (tTrInvoicePage.getSize() == 0)
            return null;

        paginatedInvoiceList = new PaginatedEntity();
        invoiceDTOList = new ArrayList<>();

        for (TTrInvoice tTrInvoice : tTrInvoicePage) {
            invoiceDTOList.add(InvoiceMapper.INSTANCE.entityToDTO(tTrInvoice));
        }

        paginatedInvoiceList.setTotalNoOfPages(tTrInvoicePage.getTotalPages());
        paginatedInvoiceList.setTotalNoOfRecords(tTrInvoicePage.getTotalElements());
        paginatedInvoiceList.setEntities(invoiceDTOList);

        return paginatedInvoiceList;
    }

    private void removeBlankPage(List<JRPrintPage> pages) {
        pages.removeIf(page -> page.getElements().isEmpty());
    }

    private void insertToInvoiceDet(Long reservationId, TTrInvoice createdInvoice) {

        final ReservationDTO reservationDTO = reservationService.getReservationById(reservationId);

        final TMsReservation tMsReservation = reservationRepository.findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds());

        List<TTrInvoiceDet> tTrInvoiceDetList = new ArrayList<>();

        final TRfBranch tRfBranch = branchRepository.findByBrnhIdAndBrnhStatus(captureBranchIds().get(0), STATUS_ACTIVE.getShortValue());

        reservationDTO.getRoomReservationList().forEach(roomReservationDTO -> {

            TTrInvoiceDet tTrInvoiceDet = new TTrInvoiceDet();

            tTrInvoiceDet.setInvoice(createdInvoice);
            tTrInvoiceDet.setBranch(tRfBranch);
            tTrInvoiceDet.setIndtStatus(STATUS_ACTIVE.getShortValue());
            tTrInvoiceDet.setReservation(tMsReservation);
            tTrInvoiceDet.setRoomReservation(roomReservationRepository.getById(roomReservationDTO.getRoomReservationId()));
            tTrInvoiceDet.setIndtUnitPrice(roomReservationDTO.getRoom().getRoomPrice());
            tTrInvoiceDet.setIndtReservedQuantity(1);
            tTrInvoiceDet.setIndtAmount(roomReservationDTO.getRoom().getRoomPrice());

            tTrInvoiceDetList.add(tTrInvoiceDet);

        });

        reservationDTO.getFacilityReservationList().forEach(facilityReservationDTO -> {

            TTrInvoiceDet tTrInvoiceDet = new TTrInvoiceDet();

            tTrInvoiceDet.setInvoice(createdInvoice);
            tTrInvoiceDet.setBranch(tRfBranch);
            tTrInvoiceDet.setIndtStatus(STATUS_ACTIVE.getShortValue());
            tTrInvoiceDet.setReservation(tMsReservation);
            tTrInvoiceDet.setFacilityReservation(facilityReservationRepository.getById(facilityReservationDTO.getFacilityReservationId()));
            tTrInvoiceDet.setIndtUnitPrice(facilityReservationDTO.getFacility().getPrice());
            tTrInvoiceDet.setIndtReservedQuantity(facilityReservationDTO.getQuantity().intValue());
            tTrInvoiceDet.setIndtAmount(facilityReservationDTO.getFacility().getPrice().multiply(facilityReservationDTO.getQuantity()));

            tTrInvoiceDetList.add(tTrInvoiceDet);
        });

        reservationDTO.getItemReservationList().forEach(itemReservationDTO -> {

            TTrInvoiceDet tTrInvoiceDet = new TTrInvoiceDet();

            tTrInvoiceDet.setInvoice(createdInvoice);
            tTrInvoiceDet.setBranch(tRfBranch);
            tTrInvoiceDet.setIndtStatus(STATUS_ACTIVE.getShortValue());
            tTrInvoiceDet.setReservation(tMsReservation);
            tTrInvoiceDet.setItemReservation(itemReservationRepository.getById(itemReservationDTO.getItemReservationId()));
            tTrInvoiceDet.setIndtUnitPrice(itemReservationDTO.getItem().getPrice());
            tTrInvoiceDet.setIndtReservedQuantity(itemReservationDTO.getQuantity().intValue());
            tTrInvoiceDet.setIndtAmount(itemReservationDTO.getItem().getPrice().multiply(itemReservationDTO.getQuantity()));

            tTrInvoiceDetList.add(tTrInvoiceDet);
        });

        invoiceDetRepository.saveAll(tTrInvoiceDetList);
    }

    private void calculateInvoiceAmounts(InvoiceDTO invoiceDTO) {

        final BigDecimal HUNDRAD = BigDecimal.valueOf(100.00);
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal netAmount = BigDecimal.ZERO;

        if(invoiceDTO.getGrossAmount() == null)
            throw new OperationException("Gross Amount is Required");

        if(invoiceDTO.getTaxPercentage() != null && invoiceDTO.getTaxAmount() == null)
            taxAmount = invoiceDTO.getGrossAmount().multiply(invoiceDTO.getTaxPercentage()).divide(HUNDRAD, 4, RoundingMode.HALF_UP);
        else if(invoiceDTO.getTaxPercentage() == null && invoiceDTO.getTaxAmount() != null)
            taxAmount = invoiceDTO.getTaxAmount();

        if(invoiceDTO.getDiscountPercentage() != null && invoiceDTO.getDiscountAmount() == null)
            discountAmount = invoiceDTO.getGrossAmount().multiply(invoiceDTO.getDiscountPercentage()).divide(HUNDRAD, 4, RoundingMode.HALF_UP);
        else if(invoiceDTO.getDiscountPercentage() == null && invoiceDTO.getDiscountAmount() != null)
            discountAmount = invoiceDTO.getDiscountAmount();

        netAmount = invoiceDTO.getGrossAmount().add(taxAmount).subtract(discountAmount);

        invoiceDTO.setTaxAmount(taxAmount);
        invoiceDTO.setDiscountAmount(discountAmount);
        invoiceDTO.setNetAmount(netAmount);
    }

    private TMsReservation validateReservationById(Long reservationId) {

        if(reservationId == null)
            throw new NoRequiredInfoException("Reservation Id is required " + reservationId);

        final TMsReservation tMsReservation = reservationRepository
                .findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds());

        if(tMsReservation == null)
            throw new DataNotFoundException("Reservation not found for the Id " + reservationId);

        return tMsReservation;
    }

    private TTrInvoice persistEntity(TTrInvoice tTrInvoice) {
        try {
            validateEntity(tTrInvoice);
            return invoiceRepository.save(tTrInvoice);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
