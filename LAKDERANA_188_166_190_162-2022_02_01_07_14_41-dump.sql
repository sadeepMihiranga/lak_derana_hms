--
-- PostgreSQL database dump
--

-- Dumped from database version 13.3
-- Dumped by pg_dump version 13.3 (Ubuntu 13.3-1.pgdg20.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: AUDIT; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "AUDIT";


ALTER SCHEMA "AUDIT" OWNER TO postgres;

--
-- Name: LAKDERANA_BASE; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "LAKDERANA_BASE";


ALTER SCHEMA "LAKDERANA_BASE" OWNER TO postgres;

--
-- Name: F_CM_GEN_TAB_SEQUENCE(character varying); Type: FUNCTION; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE FUNCTION "LAKDERANA_BASE"."F_CM_GEN_TAB_SEQUENCE"(p_table_name character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$
DECLARE
    V_SEQ_NO        character varying(30);
    V_CURR_YEAR     character varying(4);
    V_SEQ_VAL       NUMERIC(12);
BEGIN
    IF p_table_name = 'T_MS_PARTY_TOKEN' THEN
      SELECT NEXTVAL('"LAKDERANA_BASE"."S_CM_MS_PARTY_TOKEN"') INTO V_SEQ_VAL;
    ELSE
      V_SEQ_VAL := 0;
    END IF;
    SELECT TO_CHAR(CURRENT_DATE,'YYMM') INTO V_CURR_YEAR;
    V_SEQ_NO := LPAD(TO_CHAR(V_SEQ_VAL,'FM999999999999'),12,'0');
    V_SEQ_NO := 'SOLRL'||V_CURR_YEAR||V_SEQ_NO;
    RETURN V_SEQ_NO;
END;
$$;


ALTER FUNCTION "LAKDERANA_BASE"."F_CM_GEN_TAB_SEQUENCE"(p_table_name character varying) OWNER TO postgres;

--
-- Name: FUNCTION "F_CM_GEN_TAB_SEQUENCE"(p_table_name character varying); Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON FUNCTION "LAKDERANA_BASE"."F_CM_GEN_TAB_SEQUENCE"(p_table_name character varying) IS 'Generates Table Sequences for LAKDERANA BASE database tables.';


--
-- Name: F_GEN_SYS_REF_NUMBER(character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying); Type: FUNCTION; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE FUNCTION "LAKDERANA_BASE"."F_GEN_SYS_REF_NUMBER"(p_ref_num_type character varying, p_increase_param character varying, p_sub_type_ref_1 character varying, p_sub_type_ref_2 character varying, p_sub_type_ref_3 character varying, p_sub_type_ref_4 character varying, p_ref_year character varying, p_ref_month character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$
    --p_ref_num_type : CU-Customer Code, IV-Invoice Code
    --p_increase_param : Y-Increase the Parameter, N-Do not Increase the Parameter
DECLARE
    V_REF_NO             character varying(30);
    V_CURR_YEAR          character varying(4);
    V_NUM_VAL            NUMERIC(12);
    V_SUB_TYPE_REF_1     character varying(20);
    V_SUB_TYPE_REF_2     character varying(20);
    V_SUB_TYPE_REF_3     character varying(20);
    V_SUB_TYPE_REF_4     character varying(20);
    V_REF_YEAR           character varying(20);
    V_REF_MONTH          character varying(20);
    V_LEN_SUB_TYPE_REF_1 INTEGER;
    V_LEN_SUB_TYPE_REF_2 INTEGER;
    V_LEN_SUB_TYPE_REF_3 INTEGER;
    V_LEN_SUB_TYPE_REF_4 INTEGER;
    V_LEN_REF_YEAR       INTEGER;
    V_LEN_REF_MONTH      INTEGER;
    V_LEN_NUM_VAL        NUMERIC(12);

BEGIN

    V_NUM_VAL := 0;
    V_SUB_TYPE_REF_1 := p_sub_type_ref_1;
    V_SUB_TYPE_REF_2 := p_sub_type_ref_2;
    V_SUB_TYPE_REF_3 := p_sub_type_ref_3;
    V_SUB_TYPE_REF_4 := p_sub_type_ref_4;
    V_REF_YEAR := p_ref_year;
    V_REF_MONTH := p_ref_month;

    V_LEN_SUB_TYPE_REF_1 := 3;
    V_LEN_SUB_TYPE_REF_2 := 3;
    V_LEN_SUB_TYPE_REF_3 := 3;
    V_LEN_SUB_TYPE_REF_4 := 6;
    V_LEN_REF_YEAR := 2;
    V_LEN_REF_MONTH := 2;
    V_LEN_NUM_VAL := 6;

    IF p_ref_num_type = 'CU' THEN
        V_LEN_NUM_VAL := 8;
        SELECT MAX("NMPR_CUST_CD_PARAM")
        INTO V_NUM_VAL
        FROM "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"
        WHERE "NMPR_YEAR" = V_REF_YEAR
          AND "NMPR_MONTH" = V_REF_MONTH
          AND "NMPR_SLOC_CODE" = V_SUB_TYPE_REF_1
          AND "NMPR_CLAS_CODE" = V_SUB_TYPE_REF_2
          AND "NMPR_PROD_CODE" = V_SUB_TYPE_REF_3
          AND "NMPR_ACCT_CODE" = V_SUB_TYPE_REF_4;
        V_NUM_VAL := V_NUM_VAL + 1;
        IF p_increase_param = 'Y' THEN
            UPDATE "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"
            SET "NMPR_CUST_CD_PARAM" = V_NUM_VAL
            WHERE "NMPR_YEAR" = V_REF_YEAR
              AND "NMPR_MONTH" = V_REF_MONTH
              AND "NMPR_SLOC_CODE" = V_SUB_TYPE_REF_1
              AND "NMPR_CLAS_CODE" = V_SUB_TYPE_REF_2
              AND "NMPR_PROD_CODE" = V_SUB_TYPE_REF_3
              AND "NMPR_ACCT_CODE" = V_SUB_TYPE_REF_4;
        END IF;
        V_REF_NO := LPAD(TO_CHAR(V_NUM_VAL, 'FM9999999999'), CAST(V_LEN_NUM_VAL AS INT), '0');
        V_REF_NO := 'CC' || V_REF_NO;
    ELSEIF p_ref_num_type = 'IV' THEN
        V_LEN_NUM_VAL := 10;
        SELECT MAX("NMPR_PYVU_NO_PARAM")
        INTO V_NUM_VAL
        FROM "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"
        WHERE "NMPR_YEAR" = V_REF_YEAR
          AND "NMPR_MONTH" = V_REF_MONTH
          AND "NMPR_SLOC_CODE" = V_SUB_TYPE_REF_1
          AND "NMPR_CLAS_CODE" = V_SUB_TYPE_REF_2
          AND "NMPR_PROD_CODE" = V_SUB_TYPE_REF_3
          AND "NMPR_ACCT_CODE" = V_SUB_TYPE_REF_4;
        V_NUM_VAL := V_NUM_VAL + 1;
        IF p_increase_param = 'Y' THEN
            UPDATE "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"
            SET "NMPR_PYVU_NO_PARAM" = V_NUM_VAL
            WHERE "NMPR_YEAR" = V_REF_YEAR
              AND "NMPR_MONTH" = V_REF_MONTH
              AND "NMPR_SLOC_CODE" = V_SUB_TYPE_REF_1
              AND "NMPR_CLAS_CODE" = V_SUB_TYPE_REF_2
              AND "NMPR_PROD_CODE" = V_SUB_TYPE_REF_3
              AND "NMPR_ACCT_CODE" = V_SUB_TYPE_REF_4;
        END IF;
        V_REF_NO := LPAD(TO_CHAR(V_NUM_VAL, 'FM9999999999'), CAST(V_LEN_NUM_VAL AS INT), '0');
        V_SUB_TYPE_REF_1 := LPAD(V_SUB_TYPE_REF_1, CAST(V_LEN_SUB_TYPE_REF_1 AS INT), '0');
        V_SUB_TYPE_REF_2 := LPAD(V_SUB_TYPE_REF_2, CAST(V_LEN_SUB_TYPE_REF_2 AS INT), '0');
        V_SUB_TYPE_REF_3 := LPAD(V_SUB_TYPE_REF_3, CAST(V_LEN_SUB_TYPE_REF_3 AS INT), '0');
        V_SUB_TYPE_REF_4 := LPAD(V_SUB_TYPE_REF_4, CAST(V_LEN_SUB_TYPE_REF_4 AS INT), '0');
        V_REF_YEAR := RIGHT(V_REF_YEAR, CAST(V_LEN_REF_YEAR AS INT));
        --V_REF_NO := 'RM' || '/' || V_SUB_TYPE_REF_1 || '/' || V_SUB_TYPE_REF_3 || '/' || V_REF_YEAR || '/' || V_REF_NO;
        V_REF_NO := 'IV' || V_REF_NO;
    ELSE
        V_REF_NO := 'NOTAVAILABLE';
    END IF;

    RETURN V_REF_NO;
END;
$$;


ALTER FUNCTION "LAKDERANA_BASE"."F_GEN_SYS_REF_NUMBER"(p_ref_num_type character varying, p_increase_param character varying, p_sub_type_ref_1 character varying, p_sub_type_ref_2 character varying, p_sub_type_ref_3 character varying, p_sub_type_ref_4 character varying, p_ref_year character varying, p_ref_month character varying) OWNER TO postgres;

--
-- Name: FUNCTION "F_GEN_SYS_REF_NUMBER"(p_ref_num_type character varying, p_increase_param character varying, p_sub_type_ref_1 character varying, p_sub_type_ref_2 character varying, p_sub_type_ref_3 character varying, p_sub_type_ref_4 character varying, p_ref_year character varying, p_ref_month character varying); Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON FUNCTION "LAKDERANA_BASE"."F_GEN_SYS_REF_NUMBER"(p_ref_num_type character varying, p_increase_param character varying, p_sub_type_ref_1 character varying, p_sub_type_ref_2 character varying, p_sub_type_ref_3 character varying, p_sub_type_ref_4 character varying, p_ref_year character varying, p_ref_month character varying) IS 'Generates Transaction Reference Numbers';


--
-- Name: S_CM_MS_PARTY_TOKEN; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."S_CM_MS_PARTY_TOKEN"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."S_CM_MS_PARTY_TOKEN" OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: T_MS_ATTENDANCE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_ATTENDANCE" (
    "ATTN_ID" bigint NOT NULL,
    "ATTN_EMPLOYEE_CODE" character varying(12) NOT NULL,
    "ATTN_DATE" date NOT NULL,
    "ATTN_IN_TIME" time without time zone,
    "ATTN_STATUS" smallint DEFAULT 1 NOT NULL,
    "ATTN_BRANCH_ID" bigint NOT NULL,
    "ATTN_OUT_TIME" time without time zone,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10)
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_ATTENDANCE" OWNER TO postgres;

--
-- Name: T_MS_ATTENDANCE_ATTN_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_ATTENDANCE_ATTN_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_ATTENDANCE_ATTN_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_ATTENDANCE_ATTN_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_ATTENDANCE_ATTN_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_ATTENDANCE"."ATTN_ID";


--
-- Name: T_MS_DEPARTMENT; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_DEPARTMENT" (
    "DPMT_CODE" character varying(10) NOT NULL,
    "DPMT_NAME" character varying(255) NOT NULL,
    "DPMT_DESCRIPTION" text,
    "DPMT_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_DEPARTMENT" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_DEPARTMENT"."DPMT_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_DEPARTMENT"."DPMT_CODE" IS 'Identification code of a Department.';


--
-- Name: COLUMN "T_MS_DEPARTMENT"."DPMT_NAME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_DEPARTMENT"."DPMT_NAME" IS 'Name of the Department';


--
-- Name: COLUMN "T_MS_DEPARTMENT"."DPMT_DESCRIPTION"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_DEPARTMENT"."DPMT_DESCRIPTION" IS 'Description of the Department.';


--
-- Name: COLUMN "T_MS_DEPARTMENT"."DPMT_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_DEPARTMENT"."DPMT_STATUS" IS 'Active status of the Department.
1-Active, 0-Inactive';


--
-- Name: T_MS_FACILITY; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_FACILITY" (
    "FCLT_ID" bigint NOT NULL,
    "FCLT_NAME" character varying(255) NOT NULL,
    "FCLT_DESCRIPTION" text,
    "FCLT_TYPE" character varying(10) NOT NULL,
    "FCLT_PRICE" numeric(10,2),
    "FLCT_UOM" character varying(10) NOT NULL,
    "FCLT_STATUS" smallint DEFAULT 1 NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "FCLT_BRANCH_ID" bigint NOT NULL,
    "FLCT_MAX_CAPACITY" integer NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_FACILITY" OWNER TO postgres;

--
-- Name: T_MS_FACILITY_FCLT_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_FACILITY_FCLT_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_FACILITY_FCLT_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_FACILITY_FCLT_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_FACILITY_FCLT_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_FACILITY"."FCLT_ID";


--
-- Name: T_MS_FUNCTION; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_FUNCTION" (
    "FUNC_ID" character varying(20) NOT NULL,
    "FUNC_STATUS" smallint DEFAULT 1 NOT NULL,
    "FUNC_DESCRIPTION" character varying(500)
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_FUNCTION" OWNER TO postgres;

--
-- Name: TABLE "T_MS_FUNCTION"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON TABLE "LAKDERANA_BASE"."T_MS_FUNCTION" IS 'Master list of Functions Used in the System';


--
-- Name: COLUMN "T_MS_FUNCTION"."FUNC_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_FUNCTION"."FUNC_ID" IS 'Primary Key. Identification code of the Function.';


--
-- Name: COLUMN "T_MS_FUNCTION"."FUNC_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_FUNCTION"."FUNC_STATUS" IS 'Active status of the Function. 1-Active, 0-Inactive';


--
-- Name: T_MS_INQUIRY; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_INQUIRY" (
    "INQR_ID" bigint NOT NULL,
    "INQR_CUSTOMER_CODE" character varying(12),
    "INQR_DATE_TIME" timestamp without time zone NOT NULL,
    "INQR_REMARKS" text,
    "INQR_STATUS" smallint DEFAULT 1 NOT NULL,
    "INQR_BRANCH_ID" bigint NOT NULL,
    "INQR_CUSTOMER_NAME" character varying(255),
    "INQR_CONTACT_NO" character varying(255),
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "INQR_TRANSFERRED_FROM" bigint,
    "INQR_TRANSFERRED_TO" bigint
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_INQUIRY" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_INQUIRY"."INQR_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_INQUIRY"."INQR_STATUS" IS 'Status of an Inquiry

0 - Deleted
1 - Created
2 - Reserved
3 - Transferred to another
4 - Canceled';


--
-- Name: COLUMN "T_MS_INQUIRY"."INQR_TRANSFERRED_FROM"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_INQUIRY"."INQR_TRANSFERRED_FROM" IS 'Branch where the inquiry were originally created';


--
-- Name: COLUMN "T_MS_INQUIRY"."INQR_TRANSFERRED_TO"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_INQUIRY"."INQR_TRANSFERRED_TO" IS 'Branch where to be transfered.';


--
-- Name: T_MS_INQUIRY_INQR_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_INQUIRY_INQR_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_INQUIRY_INQR_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_INQUIRY_INQR_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_INQUIRY_INQR_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_INQUIRY"."INQR_ID";


--
-- Name: T_MS_ITEM; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_ITEM" (
    "ITEM_ID" bigint NOT NULL,
    "ITEM_NAME" character varying(100) NOT NULL,
    "ITEM_TYPE_CODE" character varying(10) NOT NULL,
    "ITEM_PRICE" numeric(10,2) NOT NULL,
    "ITEM_UOM" character varying(50),
    "ITEM_STATUS" smallint DEFAULT 1 NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "ITEM_BRANCH_ID" bigint NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_ITEM" OWNER TO postgres;

--
-- Name: T_MS_ITEM_ITEM_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_ITEM_ITEM_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_ITEM_ITEM_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_ITEM_ITEM_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_ITEM_ITEM_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_ITEM"."ITEM_ID";


--
-- Name: T_MS_LEAVE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_LEAVE" (
    "LEAV_ID" bigint NOT NULL,
    "LEAV_EMPLOYEE_CODE" character varying(12) NOT NULL,
    "LEAV_TYPE" character varying(10) NOT NULL,
    "LEAV_REQUESTED_DATE" timestamp without time zone NOT NULL,
    "LEAV_DATE" timestamp without time zone NOT NULL,
    "LEAV_REMARKS" text,
    "LEAV_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_LEAVE" OWNER TO postgres;

--
-- Name: T_MS_LEAVE_LEAV_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_LEAVE_LEAV_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_LEAVE_LEAV_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_LEAVE_LEAV_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_LEAVE_LEAV_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_LEAVE"."LEAV_ID";


--
-- Name: T_MS_PARTY; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_PARTY" (
    "PRTY_FIRST_NAME" character varying(255) NOT NULL,
    "PRTY_LAST_NAME" character varying(255),
    "PRTY_DOB" date,
    "PRTY_ADDRESS_1" character varying(255),
    "PRTY_ADDRESS_2" character varying(255),
    "PRTY_ADDRESS_3" character varying(255),
    "PRTY_STATUS" smallint DEFAULT 1 NOT NULL,
    "PRTY_GENDER" character varying(5),
    "PRTY_TYPE" character varying(10) NOT NULL,
    "PRTY_DEPARTMENT_CODE" character varying(15),
    "PRTY_BRANCH_ID" bigint NOT NULL,
    "PRTY_NIC" character varying(25),
    "PRTY_MANAGED_BY" character varying(12),
    "PRTY_PASSPORT" character varying(25),
    "PRTY_NAME" character varying(255) NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "PRTY_CODE" character varying(10) NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_PARTY" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_PARTY"."PRTY_FIRST_NAME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_FIRST_NAME" IS 'First part of the Party Name.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_LAST_NAME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_LAST_NAME" IS 'Last part of the Party Name.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_DOB"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_DOB" IS 'Date of Birth of the Party Name.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_ADDRESS_1"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_ADDRESS_1" IS 'First part of the Party Address.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_ADDRESS_2"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_ADDRESS_2" IS 'Second part of the Party Address.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_ADDRESS_3"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_ADDRESS_3" IS 'Last part of the Party Address.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_STATUS" IS 'Active status of the Party. 
1-Active, 0-Inactive';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_GENDER"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_GENDER" IS 'Gender Code of the Party.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_TYPE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_TYPE" IS 'Type of the Party.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_DEPARTMENT_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_DEPARTMENT_CODE" IS 'Department Code which Department that the Party(Employee) has working.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_BRANCH_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_BRANCH_ID" IS 'Identification Key of a Branch. 
Foreign Key with T_RF_BRANCH table.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_NIC"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_NIC" IS 'NIC number of the Party.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_MANAGED_BY"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_MANAGED_BY" IS 'Nearest Upper level Party Code.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_PASSPORT"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_PASSPORT" IS 'Passport number of the Party.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_NAME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_NAME" IS 'Full name of the Party.';


--
-- Name: COLUMN "T_MS_PARTY"."PRTY_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY"."PRTY_CODE" IS 'Identification Code of the Party.';


--
-- Name: T_MS_PARTY_CONTACT; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_PARTY_CONTACT" (
    "PTCN_ID" bigint NOT NULL,
    "PTCN_CONTACT_TYPE" character varying(10) NOT NULL,
    "PTCN_CONTACT_NUMBER" character varying(255) NOT NULL,
    "PTCN_STATUS" integer DEFAULT 1 NOT NULL,
    "PTCN_PRTY_CODE" character varying(12) NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_PARTY_CONTACT" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_PARTY_CONTACT"."PTCN_CONTACT_TYPE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"."PTCN_CONTACT_TYPE" IS 'Type of the Contact number.';


--
-- Name: COLUMN "T_MS_PARTY_CONTACT"."PTCN_CONTACT_NUMBER"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"."PTCN_CONTACT_NUMBER" IS 'Contact number.';


--
-- Name: COLUMN "T_MS_PARTY_CONTACT"."PTCN_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"."PTCN_STATUS" IS 'Active status of the Party Contact. 
1-Active, 0-Inactive';


--
-- Name: COLUMN "T_MS_PARTY_CONTACT"."PTCN_PRTY_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"."PTCN_PRTY_CODE" IS 'Identification Code of a Party. Foreign Key with T_MS_PARTY table.';


--
-- Name: T_MS_PARTY_CONTACT_PTCN_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_PARTY_CONTACT_PTCN_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_PARTY_CONTACT_PTCN_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_PARTY_CONTACT_PTCN_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_PARTY_CONTACT_PTCN_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"."PTCN_ID";


--
-- Name: T_MS_PARTY_TOKEN; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_PARTY_TOKEN" (
    "TOKN_SEQ_NO" character varying(30) NOT NULL,
    "TOKN_PARTY_CODE" character varying(20) NOT NULL,
    "TOKN_REQUEST_TYPE" character varying(20) NOT NULL,
    "TOKN_TOKEN" character varying(500),
    "TOKN_PIN_NO" character varying(10),
    "TOKN_EXPIRY_TIME" timestamp without time zone,
    "CREATED_USER_CODE" character varying(20) NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_USER_CODE" character varying(20),
    "LAST_MOD_DATE" timestamp without time zone,
    "TOKN_STATUS" character varying(1) DEFAULT 'A'::character varying NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_PARTY_TOKEN" OWNER TO postgres;

--
-- Name: TABLE "T_MS_PARTY_TOKEN"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON TABLE "LAKDERANA_BASE"."T_MS_PARTY_TOKEN" IS 'Registry of Tokens and Pin Numbers Generated foer All Stake Holders (Customers, Intermediaries, External Parties, etc.)';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."TOKN_SEQ_NO"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."TOKN_SEQ_NO" IS 'Primary Key. Identification Sequence of the Token record.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."TOKN_PARTY_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."TOKN_PARTY_CODE" IS 'Identification Code of the Party. List from Relevant Table as per Party Type.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."TOKN_REQUEST_TYPE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."TOKN_REQUEST_TYPE" IS 'Type of the Token Request. "PW"-Password Reset Request, "MV"-Mobile Number Verification';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."TOKN_TOKEN"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."TOKN_TOKEN" IS 'Token String Generated for Email based Recovery.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."TOKN_PIN_NO"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."TOKN_PIN_NO" IS 'Pin Number Generated for Mobile based Recovery.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."TOKN_EXPIRY_TIME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."TOKN_EXPIRY_TIME" IS 'Time by when the Token or Pin Number will Expire.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."CREATED_USER_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."CREATED_USER_CODE" IS 'Code of the User, who created the record.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."CREATED_DATE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."CREATED_DATE" IS 'Date when the record been created.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."LAST_MOD_USER_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."LAST_MOD_USER_CODE" IS 'Code of the User, who modified the record last.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."LAST_MOD_DATE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."LAST_MOD_DATE" IS 'Date when the record been modified last.';


--
-- Name: COLUMN "T_MS_PARTY_TOKEN"."TOKN_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"."TOKN_STATUS" IS 'A - Active, I - Inactive';


--
-- Name: T_MS_RESERVATION; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_RESERVATION" (
    "RESV_ID" bigint NOT NULL,
    "RESV_INQUIRY_ID" bigint NOT NULL,
    "RESV_CHECK_IN_DATE_TIME" timestamp without time zone,
    "RESV_REMARKS" text,
    "RESV_STATUS" smallint DEFAULT 1 NOT NULL,
    "RESV_NO_OF_ADULTS" integer NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "RESV_CHECK_OUT_DATE_TIME" timestamp without time zone,
    "RESV_BRANCH_ID" bigint NOT NULL,
    "RESV_CANCELLATION_REASON" text,
    "RESV_NO_OF_CHILDREN" integer
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_RESERVATION" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_RESERVATION"."RESV_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_RESERVATION"."RESV_STATUS" IS 'Status of a Reservation
   0 - Cancelled
   1 - Confirmed
   2 - Release
';


--
-- Name: COLUMN "T_MS_RESERVATION"."RESV_NO_OF_ADULTS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_RESERVATION"."RESV_NO_OF_ADULTS" IS 'No of Adult Persons that are included in the Reservation.';


--
-- Name: COLUMN "T_MS_RESERVATION"."RESV_BRANCH_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_RESERVATION"."RESV_BRANCH_ID" IS 'Reserved on which Branch. Forign Key with T_RD_BRANCH table.';


--
-- Name: COLUMN "T_MS_RESERVATION"."RESV_CANCELLATION_REASON"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_RESERVATION"."RESV_CANCELLATION_REASON" IS 'Reason to cancel the Reservation.';


--
-- Name: T_MS_RESERVATION_RESV_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_RESERVATION_RESV_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_RESERVATION_RESV_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_RESERVATION_RESV_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_RESERVATION_RESV_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_RESERVATION"."RESV_ID";


--
-- Name: T_MS_ROLE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_ROLE" (
    "ROLE_ID" bigint NOT NULL,
    "ROLE_NAME" character varying(255) NOT NULL,
    "ROLE_STATUS" smallint DEFAULT 1 NOT NULL,
    "ROLE_DESCRIPTION" character varying(255)
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_ROLE" OWNER TO postgres;

--
-- Name: TABLE "T_MS_ROLE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON TABLE "LAKDERANA_BASE"."T_MS_ROLE" IS 'User roles that are used in the system';


--
-- Name: COLUMN "T_MS_ROLE"."ROLE_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROLE"."ROLE_ID" IS 'Primary Key. Identification code of the Role.';


--
-- Name: COLUMN "T_MS_ROLE"."ROLE_NAME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROLE"."ROLE_NAME" IS 'Name of the role';


--
-- Name: COLUMN "T_MS_ROLE"."ROLE_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROLE"."ROLE_STATUS" IS 'Active status of the Function. 1-Active, 0-Inactive';


--
-- Name: COLUMN "T_MS_ROLE"."ROLE_DESCRIPTION"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROLE"."ROLE_DESCRIPTION" IS 'Description of the role';


--
-- Name: T_MS_ROLE_FUNCTION; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION" (
    "ROFU_ID" bigint NOT NULL,
    "ROFU_ROLE_ID" bigint NOT NULL,
    "ROFU_FUNC_ID" character varying(20) NOT NULL,
    "ROFU_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_ROLE_FUNCTION"."ROFU_ROLE_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION"."ROFU_ROLE_ID" IS 'Foreign Key with T_MS_ROLE table.';


--
-- Name: COLUMN "T_MS_ROLE_FUNCTION"."ROFU_FUNC_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION"."ROFU_FUNC_ID" IS 'Foreign Key with T_MS_FUNCTION table.';


--
-- Name: COLUMN "T_MS_ROLE_FUNCTION"."ROFU_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION"."ROFU_STATUS" IS 'Active status of the Roles and Functions combination.
1-Active, 0-Inactive';


--
-- Name: T_MS_ROLE_FUNCTION_ROFU_Id_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION_ROFU_Id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION_ROFU_Id_seq" OWNER TO postgres;

--
-- Name: T_MS_ROLE_FUNCTION_ROFU_Id_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION_ROFU_Id_seq" OWNED BY "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION"."ROFU_ID";


--
-- Name: T_MS_ROLE_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_ROLE_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_ROLE_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_ROLE_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_ROLE_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_ROLE"."ROLE_ID";


--
-- Name: T_MS_ROOM; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_ROOM" (
    "ROOM_ID" bigint NOT NULL,
    "ROOM_TYPE" character varying(10) NOT NULL,
    "ROOM_PRICE" numeric(10,2) NOT NULL,
    "ROOM_STATUS" smallint DEFAULT 1 NOT NULL,
    "ROOM_BRANCH_ID" bigint NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "ROOM_DESCRIPTION" text,
    "ROOM_NO" character varying(10),
    "ROOM_BED_TYPE" character varying(10)
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_ROOM" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_ROOM"."ROOM_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_ROOM"."ROOM_STATUS" IS 'Status of a Room
   0 - Inactive
   1 - Ready for Booking
   2 - Reserved
   3 - In Use
   4 - On Maintenance
   5 - Removed';


--
-- Name: T_MS_ROOM_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_ROOM_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_ROOM_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_ROOM_ROOM_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_ROOM_ROOM_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_ROOM_ROOM_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_ROOM_ROOM_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_ROOM_ROOM_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_ROOM"."ROOM_ID";


--
-- Name: T_MS_SALARY; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_SALARY" (
    "SALR_ID" bigint NOT NULL,
    "SALR_EMPLOYEE_CODE" character varying(12) NOT NULL,
    "SALR_MONTH" character varying(10) NOT NULL,
    "SALR_BASIC_SALARY" numeric(10,2) NOT NULL,
    "SALR_ALLOWANCE" numeric(10,2),
    "SALR_DEDUCTION" numeric(10,2),
    "SALR_GROSS_SALARY" numeric(10,2) NOT NULL,
    "SALR_NET_SALARY" numeric(10,2) NOT NULL,
    "SALR_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_SALARY" OWNER TO postgres;

--
-- Name: T_MS_SALARY_SALR_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_SALARY_SALR_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_SALARY_SALR_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_SALARY_SALR_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_SALARY_SALR_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_SALARY"."SALR_ID";


--
-- Name: T_MS_USER; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_USER" (
    "USER_ID" bigint NOT NULL,
    "USER_PASSWORD" character varying(255) NOT NULL,
    "USER_USERNAME" character varying(50) NOT NULL,
    "USER_PARTY_CODE" character varying(12) NOT NULL,
    "USER_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_USER" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_USER"."USER_PASSWORD"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER"."USER_PASSWORD" IS 'Password of the User.';


--
-- Name: COLUMN "T_MS_USER"."USER_USERNAME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER"."USER_USERNAME" IS 'Login Username of the User.';


--
-- Name: COLUMN "T_MS_USER"."USER_PARTY_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER"."USER_PARTY_CODE" IS 'Identification code of the Party.
Foreign Key with T_MS_PARTY table.';


--
-- Name: COLUMN "T_MS_USER"."USER_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER"."USER_STATUS" IS 'Active status of the User. 
1-Active, 0-Inactive';


--
-- Name: T_MS_USER_BRANCH; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_USER_BRANCH" (
    "USBR_ID" bigint NOT NULL,
    "USBR_USER_ID" bigint NOT NULL,
    "USBR_BRANCH_ID" bigint NOT NULL,
    "USBR_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_USER_BRANCH" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_USER_BRANCH"."USBR_USER_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER_BRANCH"."USBR_USER_ID" IS 'Identification Key of a User. Foreign Key with T_MS_USER table.';


--
-- Name: COLUMN "T_MS_USER_BRANCH"."USBR_BRANCH_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER_BRANCH"."USBR_BRANCH_ID" IS 'Identification Key of a Branch. Foreign Key with T_MS_BRACNH table.';


--
-- Name: COLUMN "T_MS_USER_BRANCH"."USBR_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER_BRANCH"."USBR_STATUS" IS 'Active status of the User and Branch Config. 
1-Active, 0-Inactive';


--
-- Name: T_MS_USER_BRANCH_USBR_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_USER_BRANCH_USBR_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_USER_BRANCH_USBR_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_USER_BRANCH_USBR_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_USER_BRANCH_USBR_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_USER_BRANCH"."USBR_ID";


--
-- Name: T_MS_USER_ROLE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_MS_USER_ROLE" (
    "USRL_USER_ID" bigint NOT NULL,
    "USRL_ROLE_ID" bigint NOT NULL,
    "USRL_ID" bigint NOT NULL,
    "USRL_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_MS_USER_ROLE" OWNER TO postgres;

--
-- Name: COLUMN "T_MS_USER_ROLE"."USRL_USER_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER_ROLE"."USRL_USER_ID" IS 'Identification Key of a User.
Foreign Key with T_MS_USER table.';


--
-- Name: COLUMN "T_MS_USER_ROLE"."USRL_ROLE_ID"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER_ROLE"."USRL_ROLE_ID" IS 'Identification Key of a Role.
Foreign Key with T_MS_ROLE table.';


--
-- Name: COLUMN "T_MS_USER_ROLE"."USRL_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_MS_USER_ROLE"."USRL_STATUS" IS 'Active status of the User and Role Config. 
1-Active, 0-Inactive';


--
-- Name: T_MS_USER_USER_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_MS_USER_USER_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_MS_USER_USER_ID_seq" OWNER TO postgres;

--
-- Name: T_MS_USER_USER_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_MS_USER_USER_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_USER"."USER_ID";


--
-- Name: T_PR_SYS_REF_NO_PARAM; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM" (
    "NMPR_SEQ_NO" character varying(30) NOT NULL,
    "NMPR_YEAR" character varying(4),
    "NMPR_MONTH" character varying(2),
    "NMPR_SLOC_CODE" character varying(20),
    "NMPR_CLAS_CODE" character varying(20),
    "NMPR_PROD_CODE" character varying(20),
    "NMPR_ACCT_CODE" character varying(20),
    "NMPR_CUST_CD_PARAM" numeric(12,0) DEFAULT 0 NOT NULL,
    "CREATED_USER_CODE" character varying(20) NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_USER_CODE" character varying(20),
    "LAST_MOD_DATE" timestamp without time zone,
    "NMPR_RCPT_NO_PARAM" numeric(12,0) DEFAULT 0,
    "NMPR_PYVU_NO_PARAM" numeric(12,0) DEFAULT 0,
    "VERSION" integer NOT NULL,
    "NMPR_ROOM_NO_PARAM" numeric(12,0)
);


ALTER TABLE "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM" OWNER TO postgres;

--
-- Name: TABLE "T_PR_SYS_REF_NO_PARAM"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON TABLE "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM" IS 'list of all Reference Number Parameters of the System';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_SEQ_NO"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_SEQ_NO" IS 'Identification code of the Reference Number Parameter';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_YEAR"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_YEAR" IS 'Year, to which the parameters are attached with';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_MONTH"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_MONTH" IS 'Month, to which the parameters are attached with';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_SLOC_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_SLOC_CODE" IS 'Identification code of the Branch, to which the parameters are attached with';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_CUST_CD_PARAM"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_CUST_CD_PARAM" IS 'Number Parameter Value for Customer Code';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."CREATED_USER_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."CREATED_USER_CODE" IS 'Code of the User, who created the record.';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."CREATED_DATE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."CREATED_DATE" IS 'Date when the record been created.';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."LAST_MOD_USER_CODE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."LAST_MOD_USER_CODE" IS 'Code of the User, who modified the record last.';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."LAST_MOD_DATE"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."LAST_MOD_DATE" IS 'Date when the record been modified last.';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_RCPT_NO_PARAM"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_RCPT_NO_PARAM" IS 'Number Parameter Value for Receipt Number';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_PYVU_NO_PARAM"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_PYVU_NO_PARAM" IS 'Number Parameter Value for Payment Voucher Number';


--
-- Name: COLUMN "T_PR_SYS_REF_NO_PARAM"."NMPR_ROOM_NO_PARAM"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"."NMPR_ROOM_NO_PARAM" IS '/*Number Parameter Value for Room Number*/';


--
-- Name: T_RF_BRANCH; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_RF_BRANCH" (
    "BRNH_ID" bigint NOT NULL,
    "BRNH_NAME" character varying(255) NOT NULL,
    "BRNH_LOCATION" character varying(500) NOT NULL,
    "BRNH_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_RF_BRANCH" OWNER TO postgres;

--
-- Name: COLUMN "T_RF_BRANCH"."BRNH_NAME"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_RF_BRANCH"."BRNH_NAME" IS 'Name of the Branch(Hotel) under Hotel chain.';


--
-- Name: COLUMN "T_RF_BRANCH"."BRNH_LOCATION"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_RF_BRANCH"."BRNH_LOCATION" IS 'Branch(Hotel) location.';


--
-- Name: COLUMN "T_RF_BRANCH"."BRNH_STATUS"; Type: COMMENT; Schema: LAKDERANA_BASE; Owner: postgres
--

COMMENT ON COLUMN "LAKDERANA_BASE"."T_RF_BRANCH"."BRNH_STATUS" IS 'Active status of the Branch. 
1-Active, 0-Inactive';


--
-- Name: T_RF_BRANCH_BRNH_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_RF_BRANCH_BRNH_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_RF_BRANCH_BRNH_ID_seq" OWNER TO postgres;

--
-- Name: T_RF_BRANCH_BRNH_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_RF_BRANCH_BRNH_ID_seq" OWNED BY "LAKDERANA_BASE"."T_RF_BRANCH"."BRNH_ID";


--
-- Name: T_RF_COMMON_REFERENCE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE" (
    "CMRF_CODE" character varying(10) NOT NULL,
    "CMRF_CMRT_CODE" character varying(10) NOT NULL,
    "CMRF_DESCRIPTION" character varying(255) NOT NULL,
    "CMRF_STATUS" smallint DEFAULT 1 NOT NULL,
    "CMRF_STRING_VALUE" character varying(255),
    "CMRF_NUMBER_VALUES" integer
);


ALTER TABLE "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE" OWNER TO postgres;

--
-- Name: T_RF_COMMON_REFERENCE_TYPE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE_TYPE" (
    "CMRT_CODE" character varying(10) NOT NULL,
    "CMRT_DESCRIPTION" character varying(255) NOT NULL,
    "CMRT_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE_TYPE" OWNER TO postgres;

--
-- Name: T_RF_REPORT_TYPE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_RF_REPORT_TYPE" (
    "RPTP_ID" bigint NOT NULL,
    "RPTP_CODE" character varying(10) NOT NULL,
    "RPTP_DISPLAY_NAME" character varying(255) NOT NULL,
    "RPTP_ICON" character varying(50) NOT NULL,
    "RPTP_COLOR" character varying(50) NOT NULL,
    "RPTP_STATUS" smallint DEFAULT 1 NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10)
);


ALTER TABLE "LAKDERANA_BASE"."T_RF_REPORT_TYPE" OWNER TO postgres;

--
-- Name: T_RF_REPORT_TYPE_RPTP_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_RF_REPORT_TYPE_RPTP_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_RF_REPORT_TYPE_RPTP_ID_seq" OWNER TO postgres;

--
-- Name: T_RF_REPORT_TYPE_RPTP_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_RF_REPORT_TYPE_RPTP_ID_seq" OWNED BY "LAKDERANA_BASE"."T_RF_REPORT_TYPE"."RPTP_ID";


--
-- Name: T_RF_USER_ROLES_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_RF_USER_ROLES_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_RF_USER_ROLES_ID_seq" OWNER TO postgres;

--
-- Name: T_RF_USER_ROLES_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_RF_USER_ROLES_ID_seq" OWNED BY "LAKDERANA_BASE"."T_MS_USER_ROLE"."USRL_ID";


--
-- Name: T_TR_FACILITY_RESERVATION; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION" (
    "FARE_ID" bigint NOT NULL,
    "FARE_FACILITY_ID" bigint NOT NULL,
    "FARE_RESERVATION_ID" bigint NOT NULL,
    "FARE_QUANTITY" numeric NOT NULL,
    "FARE_STATUS" smallint DEFAULT 1 NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "FARE_BRANCH_ID" bigint
);


ALTER TABLE "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION" OWNER TO postgres;

--
-- Name: T_TR_FACILITY_RESERVATION_FARE_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION_FARE_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION_FARE_ID_seq" OWNER TO postgres;

--
-- Name: T_TR_FACILITY_RESERVATION_FARE_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION_FARE_ID_seq" OWNED BY "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION"."FARE_ID";


--
-- Name: T_TR_INVOICE; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_TR_INVOICE" (
    "INVC_ID" bigint NOT NULL,
    "INVC_DESCRIPTION" text,
    "INVC_GROSS_AMOUNT" numeric(10,2) NOT NULL,
    "INVC_NET_AMOUNT" numeric(10,2) NOT NULL,
    "INVC_STATUS" smallint DEFAULT 1 NOT NULL,
    "INVC_BRANCH_ID" bigint NOT NULL,
    "INVC_DISCOUNT_AMOUNT" numeric(10,2),
    "INVC_TAX_AMOUNT" numeric(10,2),
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "INVC_NUMBER" character varying(25) NOT NULL,
    "INVC_RESERVATION_ID" bigint NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_TR_INVOICE" OWNER TO postgres;

--
-- Name: T_TR_INVOICE_DET; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_TR_INVOICE_DET" (
    "INDT_ID" bigint NOT NULL,
    "INDT_INVOICE_ID" bigint NOT NULL,
    "INDT_RESERVATION_ID" bigint NOT NULL,
    "INDT_ROOM_RESERVATION_ID" bigint,
    "INDT_FACILITY_RESERVATION_ID" bigint,
    "INDT_ITEM_RESERVATION_ID" bigint,
    "INDT_RESERVED_QUANTITY" integer NOT NULL,
    "INDT_UNIT_PRICE" numeric(10,2) NOT NULL,
    "INDT_STATUS" smallint DEFAULT 1 NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "INDT_BRANCH_ID" bigint NOT NULL,
    "INDT_AMOUNT" numeric(10,2) NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_TR_INVOICE_DET" OWNER TO postgres;

--
-- Name: T_TR_INVOICE_DET_INDT_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_TR_INVOICE_DET_INDT_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_TR_INVOICE_DET_INDT_ID_seq" OWNER TO postgres;

--
-- Name: T_TR_INVOICE_DET_INDT_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_TR_INVOICE_DET_INDT_ID_seq" OWNED BY "LAKDERANA_BASE"."T_TR_INVOICE_DET"."INDT_ID";


--
-- Name: T_TR_INVOICE_INVC_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_TR_INVOICE_INVC_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_TR_INVOICE_INVC_ID_seq" OWNER TO postgres;

--
-- Name: T_TR_INVOICE_INVC_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_TR_INVOICE_INVC_ID_seq" OWNED BY "LAKDERANA_BASE"."T_TR_INVOICE"."INVC_ID";


--
-- Name: T_TR_ITEM_RESERVATION; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION" (
    "ITRS_ID" bigint NOT NULL,
    "ITRS_ITEM_ID" bigint NOT NULL,
    "ITRS_RESERVATION_ID" bigint NOT NULL,
    "ITRS_QUANTITY" numeric(10,2) NOT NULL,
    "ITRS_STATUS" smallint DEFAULT 1 NOT NULL,
    "ITRS_BRANCH_ID" bigint NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10)
);


ALTER TABLE "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION" OWNER TO postgres;

--
-- Name: T_TR_ITEM_RESERVATION_ITRS_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION_ITRS_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION_ITRS_ID_seq" OWNER TO postgres;

--
-- Name: T_TR_ITEM_RESERVATION_ITRS_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION_ITRS_ID_seq" OWNED BY "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION"."ITRS_ID";


--
-- Name: T_TR_PAYMENT; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_TR_PAYMENT" (
    "PAYT_ID" bigint NOT NULL,
    "PAYT_RESERVATION_ID" bigint NOT NULL,
    "PAYT_DESCRIPTION" text,
    "PAYT_TYPE" character varying(10) NOT NULL,
    "PAYT_AMOUNT" numeric(10,2),
    "PAYT_STATUS" smallint DEFAULT 1 NOT NULL,
    "PAYT_BRANCH_ID" bigint NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "PAYT_CANCEL_REASON" text
);


ALTER TABLE "LAKDERANA_BASE"."T_TR_PAYMENT" OWNER TO postgres;

--
-- Name: T_TR_PAYMENT_PAYT_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_TR_PAYMENT_PAYT_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_TR_PAYMENT_PAYT_ID_seq" OWNER TO postgres;

--
-- Name: T_TR_PAYMENT_PAYT_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_TR_PAYMENT_PAYT_ID_seq" OWNED BY "LAKDERANA_BASE"."T_TR_PAYMENT"."PAYT_ID";


--
-- Name: T_TR_REPORT_HISTORY; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_TR_REPORT_HISTORY" (
    "RPHT_ID" bigint NOT NULL,
    "RPHT_REPORT_TYPE" bigint NOT NULL,
    "RPHT_STATUS" smallint DEFAULT 1 NOT NULL,
    "RPHT_FROM_DATE" timestamp without time zone,
    "RPHT_TO_DATE" timestamp without time zone,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "RPHT_BRANH_ID" bigint NOT NULL
);


ALTER TABLE "LAKDERANA_BASE"."T_TR_REPORT_HISTORY" OWNER TO postgres;

--
-- Name: T_TR_REPORT_HISTORY_RPHT_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_TR_REPORT_HISTORY_RPHT_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_TR_REPORT_HISTORY_RPHT_ID_seq" OWNER TO postgres;

--
-- Name: T_TR_REPORT_HISTORY_RPHT_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_TR_REPORT_HISTORY_RPHT_ID_seq" OWNED BY "LAKDERANA_BASE"."T_TR_REPORT_HISTORY"."RPHT_ID";


--
-- Name: T_TR_ROOM_RESERVATION; Type: TABLE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE TABLE "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION" (
    "RORE_ID" bigint NOT NULL,
    "RORE_ROOM_ID" bigint NOT NULL,
    "RORE_RESERVATION_ID" bigint NOT NULL,
    "RORE_STATUS" smallint DEFAULT 1 NOT NULL,
    "CREATED_DATE" timestamp without time zone NOT NULL,
    "LAST_MOD_DATE" timestamp without time zone,
    "CREATED_USER_CODE" character varying(10),
    "LAST_MOD_USER_CODE" character varying(10),
    "RORE_BRANCH_ID" bigint
);


ALTER TABLE "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION" OWNER TO postgres;

--
-- Name: T_TR_ROOM_RESERVATION_RORE_ID_seq; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION_RORE_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION_RORE_ID_seq" OWNER TO postgres;

--
-- Name: T_TR_ROOM_RESERVATION_RORE_ID_seq; Type: SEQUENCE OWNED BY; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER SEQUENCE "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION_RORE_ID_seq" OWNED BY "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION"."RORE_ID";


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE SEQUENCE "LAKDERANA_BASE".hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "LAKDERANA_BASE".hibernate_sequence OWNER TO postgres;

--
-- Name: T_RF_COMMON_REFERENCE_TYPE; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."T_RF_COMMON_REFERENCE_TYPE" (
    "CMRT_CODE" character varying(10) NOT NULL,
    "CMRT_DESCRIPTION" character varying(255) NOT NULL,
    "CMRT_STATUS" smallint DEFAULT 1 NOT NULL
);


ALTER TABLE public."T_RF_COMMON_REFERENCE_TYPE" OWNER TO postgres;

--
-- Name: T_MS_ATTENDANCE ATTN_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ATTENDANCE" ALTER COLUMN "ATTN_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_ATTENDANCE_ATTN_ID_seq"'::regclass);


--
-- Name: T_MS_FACILITY FCLT_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_FACILITY" ALTER COLUMN "FCLT_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_FACILITY_FCLT_ID_seq"'::regclass);


--
-- Name: T_MS_INQUIRY INQR_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_INQUIRY" ALTER COLUMN "INQR_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_INQUIRY_INQR_ID_seq"'::regclass);


--
-- Name: T_MS_ITEM ITEM_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ITEM" ALTER COLUMN "ITEM_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_ITEM_ITEM_ID_seq"'::regclass);


--
-- Name: T_MS_LEAVE LEAV_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_LEAVE" ALTER COLUMN "LEAV_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_LEAVE_LEAV_ID_seq"'::regclass);


--
-- Name: T_MS_PARTY_CONTACT PTCN_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY_CONTACT" ALTER COLUMN "PTCN_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_PARTY_CONTACT_PTCN_ID_seq"'::regclass);


--
-- Name: T_MS_RESERVATION RESV_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_RESERVATION" ALTER COLUMN "RESV_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_RESERVATION_RESV_ID_seq"'::regclass);


--
-- Name: T_MS_ROLE ROLE_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROLE" ALTER COLUMN "ROLE_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_ROLE_ID_seq"'::regclass);


--
-- Name: T_MS_ROLE_FUNCTION ROFU_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION" ALTER COLUMN "ROFU_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_ROLE_FUNCTION_ROFU_Id_seq"'::regclass);


--
-- Name: T_MS_ROOM ROOM_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROOM" ALTER COLUMN "ROOM_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_ROOM_ROOM_ID_seq"'::regclass);


--
-- Name: T_MS_SALARY SALR_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_SALARY" ALTER COLUMN "SALR_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_SALARY_SALR_ID_seq"'::regclass);


--
-- Name: T_MS_USER USER_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER" ALTER COLUMN "USER_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_USER_USER_ID_seq"'::regclass);


--
-- Name: T_MS_USER_BRANCH USBR_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER_BRANCH" ALTER COLUMN "USBR_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_MS_USER_BRANCH_USBR_ID_seq"'::regclass);


--
-- Name: T_MS_USER_ROLE USRL_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER_ROLE" ALTER COLUMN "USRL_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_RF_USER_ROLES_ID_seq"'::regclass);


--
-- Name: T_RF_BRANCH BRNH_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_RF_BRANCH" ALTER COLUMN "BRNH_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_RF_BRANCH_BRNH_ID_seq"'::regclass);


--
-- Name: T_RF_REPORT_TYPE RPTP_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_RF_REPORT_TYPE" ALTER COLUMN "RPTP_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_RF_REPORT_TYPE_RPTP_ID_seq"'::regclass);


--
-- Name: T_TR_FACILITY_RESERVATION FARE_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION" ALTER COLUMN "FARE_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION_FARE_ID_seq"'::regclass);


--
-- Name: T_TR_INVOICE INVC_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE" ALTER COLUMN "INVC_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_TR_INVOICE_INVC_ID_seq"'::regclass);


--
-- Name: T_TR_INVOICE_DET INDT_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET" ALTER COLUMN "INDT_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_TR_INVOICE_DET_INDT_ID_seq"'::regclass);


--
-- Name: T_TR_ITEM_RESERVATION ITRS_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION" ALTER COLUMN "ITRS_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_TR_ITEM_RESERVATION_ITRS_ID_seq"'::regclass);


--
-- Name: T_TR_PAYMENT PAYT_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_PAYMENT" ALTER COLUMN "PAYT_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_TR_PAYMENT_PAYT_ID_seq"'::regclass);


--
-- Name: T_TR_REPORT_HISTORY RPHT_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_REPORT_HISTORY" ALTER COLUMN "RPHT_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_TR_REPORT_HISTORY_RPHT_ID_seq"'::regclass);


--
-- Name: T_TR_ROOM_RESERVATION RORE_ID; Type: DEFAULT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION" ALTER COLUMN "RORE_ID" SET DEFAULT nextval('"LAKDERANA_BASE"."T_TR_ROOM_RESERVATION_RORE_ID_seq"'::regclass);


--
-- Data for Name: T_MS_ATTENDANCE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_ATTENDANCE" ("ATTN_ID", "ATTN_EMPLOYEE_CODE", "ATTN_DATE", "ATTN_IN_TIME", "ATTN_STATUS", "ATTN_BRANCH_ID", "ATTN_OUT_TIME", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE") FROM stdin;
3	CC00000002	2022-01-08	09:10:11	1	1	17:45:00	2022-01-08 17:27:00.12	2022-01-08 17:27:00.12	CC00000001	CC00000001
5	CC00000019	2022-01-08	08:08:00	1	1	20:08:00	2022-01-08 20:08:34.087	2022-01-08 20:08:34.087	CC00000001	CC00000001
4	CC00000002	2022-01-07	09:10:11	1	1	20:13:00	2022-01-08 18:10:53.734	2022-01-08 18:10:53.734	CC00000001	CC00000001
6	CC00000002	2022-01-07	09:11:12	1	1	20:13:00	2022-01-08 20:12:15.206	2022-01-08 20:12:15.206	CC00000001	CC00000001
\.


--
-- Data for Name: T_MS_DEPARTMENT; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_DEPARTMENT" ("DPMT_CODE", "DPMT_NAME", "DPMT_DESCRIPTION", "DPMT_STATUS") FROM stdin;
DEPHR	HR Department	Human Resources Department	1
DADMIN	Administrator Department	Administrator Department	1
DFROFC	Front Office Department	Front Office Department	1
DRPACC	Accounts Department	Accounts Department	1
DFDBEV	Food & Beverage Service Department	Food and Beverage Service Department	1
DINFOR	IT Department	Information Technology Department	1
\.


--
-- Data for Name: T_MS_FACILITY; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_FACILITY" ("FCLT_ID", "FCLT_NAME", "FCLT_DESCRIPTION", "FCLT_TYPE", "FCLT_PRICE", "FLCT_UOM", "FCLT_STATUS", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "FCLT_BRANCH_ID", "FLCT_MAX_CAPACITY") FROM stdin;
2	Infinity Pool	Infinity Pool	FCPOOL	2000.00	UOMHR	0	2021-12-28 08:00:44	2022-01-01 14:01:11.911	CC00000001	CC00000001	1	10
5	Car Parking	30 Cars can be parked	FCPARK	200.00	UOMHR	0	2021-12-28 08:17:53.617	2022-01-01 14:02:03.776	CC00000001	CC00000001	1	30
4	Indoor Pool	Indoor Pool	FCPOOL	2500.00	UOMHR	1	2021-12-28 08:00:44	2021-12-28 08:00:47	CC00000001	CC00000001	3	15
8	8 Ball Pool	8 Ball Pool Table	FCPOOL	300.00	UOMHR	1	2022-01-01 13:02:54.543	2022-01-01 13:05:38.778	CC00000001	CC00000001	1	8
3	Natural Pool 2	Natural Pool	FCPOOL	1000.00	UOMHR	0	2021-12-28 08:00:44	2022-01-01 13:35:33.27	CC00000001	CC00000001	2	10
7	Roof Top	Book roof top for party	FCPARK	1500.00	UOMHR	1	2022-01-01 12:53:14.597	2022-01-06 23:18:28.182	CC00000001	CC00000001	1	25
9	Safari	River side Safari	FCOTDR	600.00	UOMHR	1	2022-01-01 17:28:00.574	2022-01-06 23:53:26.219	CC00000001	CC00000001	2	25
\.


--
-- Data for Name: T_MS_FUNCTION; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_FUNCTION" ("FUNC_ID", "FUNC_STATUS", "FUNC_DESCRIPTION") FROM stdin;
CUS_CREATE	1	Customer Create
CUS_READ	1	Customer Read
INQ_CREATE	1	Inquiry Create
INQ_READ	1	Inquiry Read
RSV_CREATE	1	Reservation Create
RSV_READ	1	Reservation Read
PAY_CREATE	1	Payment Create
PAY_READ	1	Payment Read
INV_CREATE	1	Invoice Create
INV_READ	1	Invoice Read
EMP_CREATE	1	Employee Create
EMP_READ	1	Employee Read
SAL_CREATE	1	Salary Create
SAL_READ	1	Salary Read
FCL_CREATE	1	Facility Create
FCL_READ	1	Facility Read
CUS_UPDATE	1	Customer Update
CUS_DELETE	1	Customer Delete
EMP_UPDATE	1	Employee Update
EMP_DELETE	1	Employee Delete
SUP_CREATE	1	Supplier Create
SUP_READ	1	Supplier Read
MY_PROFILE_READ	1	My Profile Read
DASHBOARD_READ	1	Dashboard Read
INQ_UPDATE	1	Inquiry Update
INQ_TRANSFER	1	Inquiry Transfer
USER_READ	1	User Read
USER_DELETE	1	User Delete
USER_CREATE	1	User Create
USER_UPDATE	1	User Update
ROOM_UPDATE	1	Room Update
ROOM_DELETE	1	Room Delete
ROOM_CREATE	1	Room Create
ROOM_READ	1	Room Read
RSV_UPDATE	1	Reservation Update
RSV_DELETE	1	Reservation Delete
RSV_CANCEL	1	Reservation Cancel
FCL_UPDATE	1	Facility Update
FCL_DELETE	1	Facility Delete
RPT_READ	1	Report Read
RPT_GENERATE	1	Report Generate
RPT_REGENERATE	1	Report Re Generate
RPT_DELETE	1	Report Delete
ITM_READ	1	Item Read
ITM_CREATE	1	Item Create
ITM_UPDATE	1	Item Update
ITM_DELETE	1	Item Delete
INQ_DELETE	1	Inquiry Delete
PAY_CANCEL	1	Payment Cancel
PAY_INVOICE	1	Payment Invoice
ATT_READ	1	Attendance Read
ATT_CREATE	1	Attendance Create
LVE_CREATE	1	Leave Create
LVE_READ	1	Leave Read
\.


--
-- Data for Name: T_MS_INQUIRY; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_INQUIRY" ("INQR_ID", "INQR_CUSTOMER_CODE", "INQR_DATE_TIME", "INQR_REMARKS", "INQR_STATUS", "INQR_BRANCH_ID", "INQR_CUSTOMER_NAME", "INQR_CONTACT_NO", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "INQR_TRANSFERRED_FROM", "INQR_TRANSFERRED_TO") FROM stdin;
10	\N	2021-12-19 22:42:16.952811	She need to stay	1	1	Thamali Dineshika	0767676766	2021-12-19 22:42:17.954	2021-12-19 22:42:17.954	ADMIN	ADMIN	\N	\N
59	\N	2022-01-03 23:13:27.798047	New res	1	1	Amila	078787876	2022-01-03 23:13:27.836	2022-01-03 23:13:27.836	CC00000001	CC00000001	\N	\N
60	CC00000023	2022-01-04 22:58:55.008989		4	1	Amali Shanika	0772388895	2022-01-04 22:58:55.042	2022-01-04 22:59:10.688	CC00000001	CC00000001	\N	\N
17	\N	2021-12-25 17:44:51.127997	She need to stay	1	2	Thamali Dineshika	0767676766	2021-12-25 17:44:51.131	2021-12-25 17:44:51.131	CC00000001	CC00000001	1	\N
12	\N	2021-12-25 16:15:37.089681	She need to stay	4	1	Thamali Dineshika	0767676766	2021-12-25 16:15:37.138	2021-12-26 15:04:47.257	CC00000001	CC00000001	\N	\N
2	CC00000003	2021-12-19 21:44:18	He need to stay	1	1	Randima Suranja Liyange	12345	2021-12-19 21:45:10	2021-12-26 12:59:10.956	CC00000001	CC00000001	\N	\N
25	\N	2021-12-26 13:08:32.837803	He need to stay	1	2	Nalinda	0787878787	2021-12-26 13:08:32.838	2021-12-26 13:08:32.838	CC00000001	CC00000001	1	\N
41	\N	2021-12-31 06:25:13.606706	\N	1	1	Danith asanka	0712222321	2021-12-31 06:25:13.62	2021-12-31 06:25:13.62	CC00000001	CC00000001	\N	\N
44	\N	2021-12-31 23:45:27.240494	New res	1	1	Amila	078787876	2021-12-31 23:45:27.241	2021-12-31 23:45:27.241	CC00000001	CC00000001	\N	\N
45	\N	2022-01-01 00:02:50.611354	New res	1	1	Amila	078787876	2022-01-01 00:02:50.654	2022-01-01 00:02:50.654	CC00000001	CC00000001	\N	\N
50	CC00000018	2022-01-01 14:49:21.307103		1	1	David fernandaz	231231	2022-01-01 14:49:21.311	2022-01-01 14:49:21.311	CC00000001	CC00000001	\N	\N
52	CC00000020	2022-01-01 17:39:24.735673	scscacsac	1	1	Nuwan Sampath	94711111111	2022-01-01 17:39:24.755	2022-01-01 17:39:24.755	CC00000001	CC00000001	\N	\N
38	CC00000001	2021-12-27 02:31:39.652224	zccascscasc	4	1	Sadeep Mihiranga	94764545322	2021-12-27 02:31:39.653	2022-01-01 18:15:17.329	CC00000001	CC00000001	\N	\N
51	\N	2022-01-01 17:17:06.256284	i want room	4	1	Nuwan tharaka 2	0711212111	2022-01-01 17:17:06.257	2022-01-01 18:28:56.177	CC00000001	CC00000001	1	\N
13	\N	2021-12-25 16:17:22.908401	She need to stay	3	1	Thamali Dineshika	0767676766	2021-12-25 16:17:22.947	2021-12-25 17:44:50.959	CC00000001	CC00000001	\N	2
11	\N	2021-12-20 00:08:00.325712	He need to stay	3	1	Nalinda	0787878787	2021-12-20 00:08:00.362	2021-12-26 13:08:32.826	CC00000001	CC00000001	\N	2
37	\N	2021-12-26 21:00:09.423837	i want room	3	2	Nuwan tharaka 2	0711212111	2021-12-26 21:00:09.427	2022-01-01 17:17:06.237	CC00000001	CC00000001	1	2
36	\N	2021-12-26 20:59:24.370883	i want room	3	1	Nuwan tharaka	0711212111	2021-12-26 20:59:24.424	2021-12-26 21:00:09.249	CC00000001	CC00000001	\N	2
54	CC00000017	2022-01-03 19:24:19.425026		1	1	Vanidhu 	Gamage	2022-01-03 19:24:19.425	2022-01-03 19:24:19.425	CC00000001	CC00000001	\N	\N
56	CC00000032	2022-01-03 19:46:33.849341		1	1	Chamari Irosha	0772388895	2022-01-03 19:46:33.849	2022-01-03 19:46:33.849	CC00000001	CC00000001	\N	\N
75	CC00000023	2022-01-05 22:08:43.107105	With 2 children. Luxury package for 3 days	1	1	Amali Shanika	0779978999	2022-01-05 22:08:43.107	2022-01-05 22:17:07.573	CC00000001	CC00000001	\N	\N
68	CC00000018	2022-01-05 08:06:37.080502	2022 reservation	2	1	David fernandaz	071111111	2022-01-05 08:06:37.117	2022-01-05 21:44:47.641	CC00000001	CC00000001	\N	\N
74	CC00000028	2022-01-05 21:41:07.653477	With 2 children	3	1	Emily Star	0772288895	2022-01-05 21:41:07.653	2022-01-05 21:41:07.653	CC00000001	CC00000001	\N	\N
77	CC00000029	2022-01-06 14:37:44.349616		1	1	Teddy Kent	0771233345	2022-01-06 14:37:44.35	2022-01-06 14:37:44.35	CC00000001	CC00000001	\N	\N
84	\N	2022-01-06 20:27:46.39046	i want rooms	1	1	Piyumi Hansamali	0712121212	2022-01-06 20:27:46.445	2022-01-06 20:27:46.445	CC00000001	CC00000001	\N	\N
88	\N	2022-01-06 20:33:43.440064	i want rooms	1	1	Kuamara Sanath	0722222121	2022-01-06 20:33:43.486	2022-01-06 20:33:43.486	CC00000001	CC00000001	\N	\N
90	CC00000022	2022-01-06 22:38:35.239166	sdas	1	1	Amila Kavinda	123123123	2022-01-06 22:38:35.28	2022-01-06 22:38:59.085	CC00000001	CC00000001	\N	\N
91	\N	2022-01-07 01:51:32.499359	Abc 754	1	1	Liyanage	78549532122	2022-01-07 01:51:32.518	2022-01-07 01:51:32.518	CC00000001	CC00000001	\N	\N
92	\N	2022-01-08 08:16:57.434442	2022 reservation 3	2	1	Nuwan Kalpa	0765656555	2022-01-08 08:16:57.476	2022-01-08 08:16:57.962	CC00000001	CC00000001	\N	\N
83	CC00000025	2022-01-06 18:52:28.505225	Inquiry with Customer	2	1	Hashen Kavidhu	714521654562	2022-01-06 18:52:28.505	2022-01-08 12:51:28.623	CC00000001	CC00000001	\N	\N
93	CC00000051	2022-01-08 20:37:35.776638		2	1	Suranja Liyanage	0717039441	2022-01-08 20:37:35.812	2022-01-08 20:55:00.936	CC00000001	CC00000001	\N	\N
94	CC00000015	2022-01-09 12:53:54.226179	Res 01	2	1	kasuni malsha	0717452152	2022-01-09 12:53:54.257	2022-01-09 12:56:06.318	CC00000001	CC00000001	\N	\N
95	CC00000015	2022-01-09 14:09:56.953695	Rem 1	2	1	kasuni malsha	071700225	2022-01-09 14:09:56.954	2022-01-09 14:12:17.858	CC00000001	CC00000001	\N	\N
\.


--
-- Data for Name: T_MS_ITEM; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_ITEM" ("ITEM_ID", "ITEM_NAME", "ITEM_TYPE_CODE", "ITEM_PRICE", "ITEM_UOM", "ITEM_STATUS", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "ITEM_BRANCH_ID") FROM stdin;
1	Absolout Vodka	ITMHLQ	500.00	UOMHLS	1	2022-01-03 20:04:22.845	2022-01-04 17:48:57.704	CC00000001	CC00000001	1
3	ascascasc	ITMHLQ	3.00	UOMHR	0	2022-01-04 17:54:10.571	2022-01-04 17:55:35.803	CC00000001	CC00000001	1
4	Whiskey	ITMHLQ	2500.00	UOMHLS	1	2022-01-04 18:55:33.966	2022-01-04 18:55:33.966	CC00000001	CC00000001	1
5	Wine	ITMHLQ	2500.00	UOMHLS	1	2022-01-04 18:56:02.759	2022-01-04 18:56:02.759	CC00000001	CC00000001	1
6	Canned Beer	ITMHLQ	750.00	UOMHLS	1	2022-01-04 18:58:18.28	2022-01-04 18:58:18.28	CC00000001	CC00000001	1
7	White Rum	ITMHLQ	7500.00	UOMHLS	1	2022-01-04 19:02:32.492	2022-01-04 19:02:32.492	CC00000001	CC00000001	1
8	Dark Rum	ITMHLQ	6500.00	UOMHLS	1	2022-01-04 19:03:22.982	2022-01-04 19:03:22.982	CC00000001	CC00000001	1
9	CALYPSO SILVER RUM	ITMHLQ	8500.00	UOMHLS	1	2022-01-04 19:04:23.814	2022-01-04 19:04:23.814	CC00000001	CC00000001	1
10	Gold Rum	ITMHLQ	6500.00	UOMHLS	1	2022-01-04 19:05:07.47	2022-01-04 19:05:07.47	CC00000001	CC00000001	1
11	Unflavored Vodka	ITMHLQ	5000.00	UOMHLS	1	2022-01-04 19:06:31.061	2022-01-04 19:06:31.061	CC00000001	CC00000001	1
12	scasc	ITMHLQ	12312.00	UOMHR	0	2022-01-06 20:55:06.868	2022-01-06 20:56:08.068	CC00000001	CC00000001	1
2	Beer 2	ITMHLQ	260.00	UOMHLS	1	2022-01-04 17:49:17.011	2022-01-07 01:42:32.923	CC00000001	CC00000001	1
\.


--
-- Data for Name: T_MS_LEAVE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_LEAVE" ("LEAV_ID", "LEAV_EMPLOYEE_CODE", "LEAV_TYPE", "LEAV_REQUESTED_DATE", "LEAV_DATE", "LEAV_REMARKS", "LEAV_STATUS") FROM stdin;
\.


--
-- Data for Name: T_MS_PARTY; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_PARTY" ("PRTY_FIRST_NAME", "PRTY_LAST_NAME", "PRTY_DOB", "PRTY_ADDRESS_1", "PRTY_ADDRESS_2", "PRTY_ADDRESS_3", "PRTY_STATUS", "PRTY_GENDER", "PRTY_TYPE", "PRTY_DEPARTMENT_CODE", "PRTY_BRANCH_ID", "PRTY_NIC", "PRTY_MANAGED_BY", "PRTY_PASSPORT", "PRTY_NAME", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "PRTY_CODE") FROM stdin;
Namal	Gunawardana	1985-01-12	No: 23	Colombo 07	\N	1	M	EMPLY	DEPHR	1	850132503V	\N	N352356276	Namal Gunawardana	2021-12-16 08:31:57	\N	\N	\N	CC00000006
Adminstrator	Admin	\N	as	as	AS	1	M	EMPLY	DRPACC	1	990132503V	CC00000012	1655465466	Adminstrator Admin	2021-12-28 14:31:54.337	2022-01-06 22:55:37.414	CC00000001	CC00000001	CC00000019
Nilan	Lakshitha	1966-01-11	No: 15	Colombo 08	Borella	1	M	EMPLY	DEPHR	1	660112503V	\N	N6536745	Nilan Lakshitha	2021-12-16 19:08:30.235	2021-12-16 19:08:30.235	CC00000001	CC00000001	CC00000007
Rasini	Mahesha	1977-01-14	No: 15	Colombo 09	Nugegoda	1	M	EMPLY	DEPHR	1	970542503V	\N	N53287462	Rasini Mahesha	2021-12-21 06:50:04.558	2021-12-21 06:50:04.558	CC00000001	CC00000001	CC00000008
Chamari	Irosha	\N	No: 15	Colombo 08	Borella	1	F	EMPLY	DEPHR	1	760132503V	CC00000006	N68327823	Chamari Irosha	2021-12-16 08:31:57	2022-01-07 01:55:24.046	\N	CC00000001	CC00000002
Nuwan	Chamara	1965-01-12	No: 23	Colombo 07	\N	1	M	EMPLY	DEPHR	1	650132503V	\N	N323525432	Nuwan Chamara	2021-12-16 08:31:57	2021-12-25 08:04:04.649	\N	CC00000001	CC00000004
Amal	Jayasinghe	\N	asca	ascas	asc	1	M	EMPLY	DRPACC	1	ascas	CC00000008	ascasc	Amal Jayasinghe	2022-01-06 21:22:29.26	2022-01-07 01:55:40.908	CC00000001	CC00000001	CC00000041
Kasuni	Perera	\N	332/B	Park Road	Colombo	1	F	EMPLY	DEPHR	1	19985678954	CC00000004	\N	Kasuni Perera	2022-01-07 14:18:16.347	2022-01-07 14:18:16.347	CC00000001	CC00000001	CC00000042
saxa	saxa	\N	asx	sx	asx	0		CUSTM	\N	1	asafax	\N	afa	saxa saxa	2021-12-21 23:51:04.636	2021-12-27 02:43:12.532	CC00000001	CC00000001	CC00000009
Emp 	One	\N	23	Asdf	Dfrtygg	1	F	EMPLY	DRPACC	1	498795644546	\N	454545	Emp  One	2021-12-24 17:04:04.133	2021-12-24 17:04:04.133	CC00000001	CC00000001	CC00000012
Sadeep	Mihiranga	1997-01-13	13/32	Kahangama	Ratnapura	1	M	CUSTM	\N	1	970132503V	\N	N89786543	H V Sadeep Mihiranga	2021-12-16 08:31:57	\N	\N	\N	CC00000001
Randima	Suranja Liyange	1995-01-12	No: 12	Kahawatta	Ratnapura	1	M	CUSTM	\N	1	950132503V	\N	N45365173	Randima Suranja Liyange	2021-12-16 08:31:57	\N	\N	\N	CC00000003
kasuni	malsha	\N	78	Matara Road	galle	1		CUSTM	\N	1	1234567890	\N	7894561237	kasuni malsha	2021-12-28 11:03:52.317	2021-12-28 11:03:52.318	CC00000001	CC00000001	CC00000015
Kamal	Perera	\N	asc	asc	asc	0		CUSTM	\N	1	sacas	\N	asc	Kamal Perera	2021-12-23 16:36:13.981	2021-12-26 23:38:24.078	CC00000001	CC00000001	CC00000010
Rasika	Silva	\N	456	Galle Road	Colombo 05	1		CUSTM	\N	1	7894561231	\N	7418529639	Rasika Silva	2021-12-28 11:16:57.994	2021-12-28 11:17:39.317	CC00000001	CC00000001	CC00000016
Chamari	Irosha	\N	273	Circular Road	Galle	1		CUSTM	\N	1	77887788777	\N	\N	Chamari Irosha	2022-01-04 22:12:17.642	2022-01-04 22:12:17.642	CC00000001	CC00000001	CC00000033
Vanidhu	Gamage	\N	58	Galle Road	Colombo	1		CUSTM	\N	1	1970456789	\N	7894561230	Vanidhu Gamage	2021-12-28 13:57:33.221	2021-12-31 14:03:14.145	CC00000001	CC00000001	CC00000017
Tishani	kavindya	\N	741	Galle Road	Colombo	1		CUSTM	\N	1	7894563210	\N	\N	Tishani kavindya	2022-01-03 19:26:35.381	2022-01-03 19:26:35.381	CC00000001	CC00000001	CC00000024
ascasc	ascasc	\N	ascasc	scasc	ascasc	0		CUSTM	\N	1	ascasc	\N	ascsac	ascasc ascasc	2021-12-26 17:04:52.748	2022-01-01 14:02:53.898	CC00000001	CC00000001	CC00000013
Hashen	Kavidhu	\N	98	Galle Road	Colombo	1		CUSTM	\N	1	194561237	\N	\N	Hashen Kavidhu	2022-01-03 19:27:30.172	2022-01-03 19:27:30.172	CC00000001	CC00000001	CC00000025
Sadun	perera	\N	45	Matara Road 	Galle	1		CUSTM	\N	1	1982456321	\N	\N	Sadun perera	2022-01-03 19:28:43.224	2022-01-03 19:28:43.224	CC00000001	CC00000001	CC00000026
Nuwan	Sampath	\N	No12	Kanday		1		CUSTM	\N	1	980132503v	\N	\N	Nuwan Sampath	2022-01-01 14:54:33.108	2022-01-01 14:58:33.47	CC00000001	CC00000001	CC00000020
David	Smith	\N	78	Galle road	Colombo	1		CUSTM	\N	1	96325874100	\N	\N	David Smith	2022-01-03 19:32:50.204	2022-01-03 19:32:50.204	CC00000001	CC00000001	CC00000027
David	fernandaz	\N	78A	Galle Road	Colombo	1		CUSTM	\N	1	4561237890	\N	7418529630	David fernandaz	2021-12-28 14:04:24.561	2022-01-02 13:19:48.433	CC00000001	CC00000001	CC00000018
Emily	Star	\N	52	Galle road	Colombo	1		CUSTM	\N	1	7894563214	\N	\N	Emily Star	2022-01-03 19:35:36.44	2022-01-03 19:35:36.44	CC00000001	CC00000001	CC00000028
Teddy	Kent	\N	52	Colombo Road	Galle	1		CUSTM	\N	1	96321145870	\N	\N	Teddy Kent	2022-01-03 19:36:18.944	2022-01-03 19:36:18.944	CC00000001	CC00000001	CC00000029
Amila	Kavinda	\N	74/A	Matara Road	Galle	1		CUSTM	\N	1	78965412301	\N	12345	Amila Kavinda	2022-01-02 13:35:21.997	2022-01-03 19:37:19.315	CC00000001	CC00000001	CC00000022
Kamal	Perera	\N	95	Matara Road	Galle	1		CUSTM	\N	1	64654646	\N	46546	Kamal Perera	2022-01-01 15:08:35.937	2022-01-03 19:37:48.006	CC00000001	CC00000001	CC00000021
Elsy	Burnly	\N	14A	Avenue Road	Kandy	1		CUSTM	\N	1	197856123	\N	\N	Elsy Burnly	2022-01-03 19:39:18.861	2022-01-03 19:39:18.861	CC00000001	CC00000001	CC00000030
Perry 	Millar	\N	14A	Avenue Road	Kandy	1		CUSTM	\N	1	19794563214	\N	\N	Perry  Millar	2022-01-03 19:40:04.347	2022-01-03 19:40:04.347	CC00000001	CC00000001	CC00000031
Brayan	Smith	\N	93	Park Road	Kandy	1		CUSTM	\N	1	123123123	\N	12312312	Brayan Smith	2021-12-24 09:25:42.149	2022-01-03 19:41:18.732	CC00000001	CC00000001	CC00000011
Chamari	Irosha	\N	273	Circular Road	Galle	0		CUSTM	\N	1	7788778877	\N	\N	Chamari Irosha	2022-01-03 19:44:20.881	2022-01-04 22:11:12.822	CC00000001	CC00000001	CC00000032
Chamila	Madushi	\N	99	Matara Road	Galle	1		CUSTM	\N	1	19667856145	\N	\N	Chamila Madushi	2022-01-06 14:39:37.271	2022-01-06 14:39:37.271	CC00000001	CC00000001	CC00000035
Sadun	Gamage	\N	741/A	Nugegoda	Colombo	1		CUSTM	\N	1	19997845612	\N	\N	Sadun Gamage	2022-01-06 14:40:37.155	2022-01-06 14:40:37.155	CC00000001	CC00000001	CC00000036
Malithi	Mendis	\N	42	Lake Road	Kandy	1		CUSTM	\N	1	1998456123	\N	\N	Malithi Mendis	2022-01-06 14:41:48.821	2022-01-06 14:41:48.821	CC00000001	CC00000001	CC00000037
Manoj	perera	\N	52	Flower Road	Galle	1		CUSTM	\N	1	1998789456	\N	\N	Manoj perera	2022-01-06 14:43:36.443	2022-01-06 14:43:36.443	CC00000001	CC00000001	CC00000038
Shawn	Farnando	\N	66/A	Circular Road	Galle	1		CUSTM	\N	1	1999789456	\N	\N	Shawn Farnando	2022-01-06 17:45:36.048	2022-01-06 17:45:36.048	CC00000001	CC00000001	CC00000039
Amali	Shanika	\N	852	Nugegoda	Colombo	1		CUSTM	\N	1	1978456123	\N	555555	Amali Shanika	2022-01-03 19:25:36.296	2022-01-06 20:35:45.955	CC00000001	CC00000001	CC00000023
sdd	kjj	\N	4	bbjh	jhbh	0		CUSTM	\N	1	65564	\N	46544	sdd kjj	2022-01-06 12:26:59.102	2022-01-06 20:35:54.38	CC00000001	CC00000001	CC00000034
ascasc	ascasc	\N	jh	hbj	ijh	1		CUSTM	\N	1	646544564	\N	56454544	ascasc ascasc	2022-01-06 20:36:18.383	2022-01-06 20:36:18.383	CC00000001	CC00000001	CC00000040
ascasc	ascasc	\N	No 1	assaasc	sdfwefwe	0	F	EMPLY	DRPACC	1	3123123	\N	123123123	ascasc ascasc	2021-12-26 17:13:20.068	2022-01-06 20:42:11.51	CC00000001	CC00000001	CC00000014
Sanath	Jayasooriya	\N	No: 15	Colombo 08	Borella	1	M	EMPLY	DEPHR	1	660132503V	CC00000004	N653671	Sanath Jayasooriya	2021-12-16 08:31:57	2022-01-06 21:21:28.872	\N	CC00000001	CC00000005
Emily	Star	\N	78	Galle Road	Colombo	1	F	EMPLY	DRPACC	1	19994578965	CC00000004	\N	Emily Star	2022-01-07 16:18:29.865	2022-01-07 16:18:29.865	CC00000001	CC00000001	CC00000043
Danushka	Rajamanthri	\N	965	Galle Road	Colombo	1	F	EMPLY	DADMIN	1	1988459721	CC00000019	\N	Danushka Rajamanthri	2022-01-08 12:19:59.896	2022-01-08 12:19:59.896	CC00000001	CC00000001	CC00000044
Harsha Tharanga	Ariyarathna	\N	45	Circular Road	Colombo	1	F	EMPLY	DFDBEV	1	1989456523	CC00000019	\N	Harsha Tharanga Ariyarathna	2022-01-08 12:24:19.948	2022-01-08 12:24:52.725	CC00000001	CC00000001	CC00000045
Gayani	Guruge	\N	45	Park Road	Rathmalana	1	F	EMPLY	DEPHR	1	1990759523	CC00000019	\N	Gayani Guruge	2022-01-08 12:28:44.205	2022-01-08 12:28:44.205	CC00000001	CC00000001	CC00000046
Damith gayashan	Perera	\N	12A	Galle Road	Colombo	1	M	EMPLY	DFROFC	1	1991478462	CC00000019	\N	Damith gayashan Perera	2022-01-08 12:31:08.336	2022-01-08 12:31:08.336	CC00000001	CC00000001	CC00000047
Nalaka	Jayasumana	\N	51C	Main Street	Gampaha	1	M	EMPLY	DRPACC	1	1980462185	CC00000019	\N	Nalaka Jayasumana	2022-01-08 12:33:25.336	2022-01-08 12:33:25.336	CC00000001	CC00000001	CC00000048
Mahesh	De silva	\N	79	Matara Road	Galle	1	M	EMPLY	DADMIN	1	1978756243	CC00000019	\N	Mahesh De silva	2022-01-08 12:36:45.265	2022-01-08 12:36:45.265	CC00000001	CC00000001	CC00000049
Chamath	De Soyza	\N	58	High Street	Pannipitiyya	1	M	EMPLY	DADMIN	1	1984652678	CC00000019	\N	Chamath De Soyza	2022-01-08 12:39:39.442	2022-01-08 12:39:39.442	CC00000001	CC00000001	CC00000050
Suranja	Liyanage	\N	21	Makcos Rd	Kandy	1	M	CUSTM	\N	1	458542156V	\N	44894564	Suranja Liyanage	2022-01-08 20:15:54.084	2022-01-08 20:15:54.084	CC00000001	CC00000001	CC00000051
Suranja	Liyanage	\N	21	Colonna Rd	Kandy	1	M	CUSTM	\N	1	945215265	\N	655666	Suranja Liyanage	2022-01-08 20:21:27.928	2022-01-08 20:21:27.928	CC00000001	CC00000001	CC00000052
\.


--
-- Data for Name: T_MS_PARTY_CONTACT; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_PARTY_CONTACT" ("PTCN_ID", "PTCN_CONTACT_TYPE", "PTCN_CONTACT_NUMBER", "PTCN_STATUS", "PTCN_PRTY_CODE") FROM stdin;
46	CNMBL	94764545322	1	CC00000001
60	CNMBL	94764545388	1	CC00000005
17	CNMBL	94756565431	1	CC00000002
1	CNEML	saddeepmihiranga@gmail.com	1	CC00000001
2	CNMBL	94723434321	1	CC00000008
5	CNMBL	07174585652	1	CC00000012
6	CNEML	empone@qwe.com	1	CC00000012
7	CNEML	chamara@gmail.com	1	CC00000004
8	CNEML	suranjaliyanage@gmail.com	1	CC00000003
4	CNEML	abc.sd@we.cv	1	CC00000011
12	CNEML	msilva@gmail.com	0	CC00000010
13	CNMBL	071452652	0	CC00000009
9	CNMBL	23123	0	CC00000013
29	CNMBL	94711111111	1	CC00000020
3	CNMBL	07145555	1	CC00000011
31	CNMBL	071111111	1	CC00000018
30	CNMBL	045221545652	1	CC00000021
32	CNMBL	123123123	1	CC00000022
33	CNMBL	0772388895	0	CC00000032
35	CNMBL	0771244456	1	CC00000039
34	CNMBL	65454456	0	CC00000034
36	CNMBL	6456445	1	CC00000040
11	CNEML	abc@qwe.oi	0	CC00000014
10	CNMBL	msilva@gmail.com	0	CC00000014
37	CNMBL	07165465554	1	CC00000041
38	CNEML	msilva@gmail.com	1	CC00000019
39	CNMBL	0773388865	1	CC00000042
40	CNMBL	0771277785	1	CC00000043
41	CNMBL	0717039441	1	CC00000051
42	CNMBL	0717039441	1	CC00000052
\.


--
-- Data for Name: T_MS_PARTY_TOKEN; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_PARTY_TOKEN" ("TOKN_SEQ_NO", "TOKN_PARTY_CODE", "TOKN_REQUEST_TYPE", "TOKN_TOKEN", "TOKN_PIN_NO", "TOKN_EXPIRY_TIME", "CREATED_USER_CODE", "CREATED_DATE", "LAST_MOD_USER_CODE", "LAST_MOD_DATE", "TOKN_STATUS") FROM stdin;
SOLRL2112000000000007	CC00000001	PW	04a9ab27-1469-47af-8a8d-746ce9613dbd	\N	2021-12-19 16:00:13.653	CC00000001	2021-12-19 15:00:13.746	CC00000001	2021-12-19 15:17:49.011	I
SOLRL2112000000000008	CC00000001	PW	b329ea7b-a92e-452e-974e-06e69a59eda3	\N	2021-12-19 14:15:38.832	CC00000001	2021-12-19 13:15:38.842	CC00000001	2021-12-19 13:15:38.842	A
SOLRL2112000000000009	CC00000001	PW	800b3b34-0424-44e4-8c6e-8849a6586e7d	\N	2021-12-19 20:50:33.794	CC00000001	2021-12-19 19:50:33.894	CC00000001	2021-12-19 19:50:33.894	A
SOLRL2112000000000010	CC00000001	PW	419bd6df-1b31-4871-816c-c8b5f197dfc5	\N	2021-12-19 21:00:59.271	CC00000001	2021-12-19 20:00:59.362	CC00000001	2021-12-19 20:00:59.362	A
SOLRL2112000000000011	CC00000001	PW	448baf5e-b7f0-4a3f-833a-a1842542315f	\N	2021-12-19 21:03:04.36	CC00000001	2021-12-19 20:03:04.451	CC00000001	2021-12-19 20:03:04.451	A
SOLRL2112000000000012	CC00000001	PW	d156affe-fef1-4ea1-aeb8-561530a8f579	\N	2021-12-19 21:05:48.746	CC00000001	2021-12-19 20:05:48.937	CC00000001	2021-12-19 20:05:48.937	A
SOLRL2112000000000013	CC00000001	PW	32bb2459-50df-4882-bfff-74bb9c40c77f	\N	2021-12-19 21:16:25.124	CC00000001	2021-12-19 20:16:25.215	CC00000001	2021-12-19 20:16:25.215	A
SOLRL2112000000000014	CC00000001	PW	1a718c15-95d9-462b-95ce-0d047901d930	\N	2021-12-21 18:40:15.631	CC00000001	2021-12-21 17:40:15.633	CC00000001	2021-12-21 17:40:15.633	A
SOLRL2112000000000015	CC00000001	PW	6fe75486-1e33-4f55-a26b-7556625cbd81	\N	2021-12-21 20:16:46.643	CC00000001	2021-12-21 19:16:46.645	CC00000001	2021-12-21 19:16:46.645	A
SOLRL2112000000000016	CC00000001	PW	297eec60-c009-4841-a1f3-1230f7c6c66c	\N	2021-12-21 20:18:56.211	CC00000001	2021-12-21 19:18:56.218	CC00000001	2021-12-21 19:18:56.218	A
SOLRL2112000000000017	CC00000001	PW	c1bcef10-bb04-4182-915c-bd292e3f769b	\N	2021-12-21 20:19:19.406	CC00000001	2021-12-21 19:19:19.408	CC00000001	2021-12-21 19:19:19.408	A
SOLRL2112000000000018	CC00000001	PW	eb62657a-0f27-41e4-8103-370746c3b5fb	\N	2021-12-23 20:11:01.781	CC00000001	2021-12-23 19:11:01.782	CC00000001	2021-12-23 19:11:01.782	A
SOLRL2112000000000019	CC00000001	PW	56579e39-2db5-48da-8963-139420b1f292	\N	2021-12-25 05:39:40.35	CC00000001	2021-12-25 04:39:40.358	CC00000001	2021-12-25 04:39:40.358	A
SOLRL2112000000000020	CC00000003	PW	5be6d937-c205-43e0-8b33-36a5bd7964f9	\N	2021-12-25 18:22:59.767	CC00000003	2021-12-25 17:22:59.781	CC00000003	2021-12-25 17:22:59.781	A
SOLRL2112000000000021	CC00000001	PW	9547879b-84b5-4be0-83df-0b801b3ac0cd	\N	2021-12-26 04:11:26.194	CC00000001	2021-12-26 03:11:26.197	CC00000001	2021-12-26 03:11:26.197	A
SOLRL2112000000000022	CC00000001	PW	ddd036c2-0fc5-4aef-a5bd-ccf7aeab4ee1	\N	2021-12-26 04:13:44.664	CC00000001	2021-12-26 03:13:44.665	CC00000001	2021-12-26 03:13:44.665	A
SOLRL2112000000000023	CC00000003	PW	af98e56c-63dc-4545-bc10-e68caf1adb46	\N	2021-12-26 04:14:06.098	CC00000003	2021-12-26 03:14:06.1	CC00000003	2021-12-26 03:14:06.1	A
SOLRL2112000000000024	CC00000003	PW	97db9f47-53c9-4161-9c61-6bd00eee0cea	\N	2021-12-26 04:20:47.377	CC00000003	2021-12-26 03:20:47.378	CC00000003	2021-12-26 03:20:47.378	A
SOLRL2112000000000025	CC00000003	PW	9110bfcf-e216-4a90-9b0f-d3915362d0cd	\N	2021-12-26 04:37:30.287	CC00000003	2021-12-26 03:37:30.288	CC00000003	2021-12-26 03:37:30.288	A
SOLRL2112000000000026	CC00000001	PW	c0a63ef3-7fae-492d-b9a9-58b3480f7be9	\N	2021-12-26 04:44:15.847	CC00000001	2021-12-26 03:44:15.849	CC00000001	2021-12-26 03:44:15.849	A
SOLRL2112000000000027	CC00000003	PW	b7f7afa9-1bce-46e2-9608-d8bc7946896e	\N	2021-12-26 04:56:38.154	CC00000003	2021-12-26 03:56:38.166	CC00000003	2021-12-26 03:59:44.721	I
SOLRL2112000000000028	CC00000001	PW	ffc96f82-de99-41bf-865e-4836f2f93b99	\N	2021-12-26 08:52:40.245	CC00000001	2021-12-26 07:52:40.247	CC00000001	2021-12-26 07:52:40.247	A
SOLRL2112000000000029	CC00000003	PW	8a96afb2-39b6-4faf-9b63-453bbae5f82b	\N	2021-12-26 08:58:32.926	CC00000003	2021-12-26 07:58:32.927	CC00000003	2021-12-26 07:59:16.369	I
SOLRL2112000000000030	CC00000001	PW	c6b8c710-bf73-4cf3-8d06-807b62ba393d	\N	2021-12-26 09:02:31.661	CC00000001	2021-12-26 08:02:31.662	CC00000001	2021-12-26 08:03:28.498	I
SOLRL2112000000000031	CC00000003	PW	e73c97d6-9d23-452c-939e-879d6bd35121	\N	2021-12-26 09:20:54.357	CC00000003	2021-12-26 08:20:54.357	CC00000003	2021-12-26 08:20:54.357	A
SOLRL2112000000000032	CC00000003	PW	0e356597-f19c-4d20-9942-662f46b2b1ee	\N	2021-12-26 15:02:20.94	CC00000003	2021-12-26 14:02:20.941	CC00000003	2021-12-26 14:02:20.941	A
SOLRL2112000000000033	CC00000001	PW	92c5a9d0-2ee4-4f9a-a12f-2a103b91e0fe	\N	2021-12-26 23:14:03.616	CC00000001	2021-12-26 22:14:03.729	CC00000001	2021-12-26 22:14:03.729	A
SOLRL2112000000000034	CC00000001	PW	94924f55-a755-4748-96d9-f1c9e2d5fcb8	\N	2021-12-26 23:19:46.807	CC00000001	2021-12-26 22:19:46.906	CC00000001	2021-12-26 22:19:46.906	A
SOLRL2112000000000035	CC00000003	PW	aee11ca3-8bdc-4e0f-bdd0-a191257b8fc7	\N	2021-12-27 03:35:02.059	CC00000003	2021-12-27 02:35:02.06	CC00000003	2021-12-27 02:35:02.06	A
SOLRL2112000000000036	CC00000003	PW	e25a64b5-27e3-47e6-9a92-17b4065a9f15	\N	2021-12-27 03:35:22.908	CC00000003	2021-12-27 02:35:22.915	CC00000003	2021-12-27 02:35:22.915	A
SOLRL2112000000000037	CC00000001	PW	413457b2-446b-4076-a597-15ab7d1a156d	\N	2021-12-27 03:35:43.418	CC00000001	2021-12-27 02:35:43.419	CC00000001	2021-12-27 02:35:43.419	A
SOLRL2112000000000038	CC00000001	PW	48c3d2ad-81aa-441d-abbe-dc15c26f0279	\N	2021-12-27 03:57:15.975	CC00000001	2021-12-27 02:57:15.976	CC00000001	2021-12-27 02:57:15.976	A
SOLRL2112000000000039	CC00000001	PW	60beab22-b241-437a-a033-abc5727bac5b	\N	2021-12-27 09:29:48.558	CC00000001	2021-12-27 08:29:48.65	CC00000001	2021-12-27 08:29:48.65	A
SOLRL2112000000000040	CC00000001	PW	22e24941-7ec0-420e-b8b4-b33f4f7ccc54	\N	2021-12-27 09:33:20.68	CC00000001	2021-12-27 08:33:20.772	CC00000001	2021-12-27 08:33:20.772	A
SOLRL2112000000000041	CC00000001	PW	3c5fdcdc-e4dc-4f22-ad73-db0077691ce2	\N	2021-12-27 23:35:44.536	CC00000001	2021-12-27 22:35:44.63	CC00000001	2021-12-27 22:35:44.63	A
SOLRL2112000000000042	CC00000001	PW	64a178bf-7e13-41a4-a4c6-ee24023f3420	\N	2021-12-27 23:44:05.566	CC00000001	2021-12-27 22:44:05.656	CC00000001	2021-12-27 22:44:05.656	A
SOLRL2112000000000043	CC00000001	PW	1427e161-32a5-49b3-936c-1b5b5fd35020	\N	2021-12-27 23:57:05.499	CC00000001	2021-12-27 22:57:05.591	CC00000001	2021-12-27 22:57:05.591	A
SOLRL2112000000000044	CC00000001	PW	3624cbed-0efa-4431-9466-d21a4ed75511	\N	2021-12-28 00:03:46.707	CC00000001	2021-12-27 23:03:46.798	CC00000001	2021-12-27 23:03:46.798	A
SOLRL2201000000000045	CC00000001	PW	0e53c0ab-a70d-4e21-a534-6355d2edbbf5	\N	2022-01-06 21:57:56.972	CC00000001	2022-01-06 20:57:56.972	CC00000001	2022-01-06 20:57:56.972	A
SOLRL2201000000000046	CC00000001	PW	26ae4830-59dd-4d89-939c-bb7de11b646a	\N	2022-01-08 09:39:58.705	CC00000001	2022-01-08 08:39:58.91	CC00000001	2022-01-08 08:39:58.91	A
SOLRL2201000000000047	CC00000001	PW	c6cc4737-a2ea-4461-a265-ea3ea995653d	\N	2022-01-08 09:48:43.554	CC00000001	2022-01-08 08:48:43.83	CC00000001	2022-01-08 08:48:43.83	A
SOLRL2201000000000048	CC00000001	PW	dade12c1-4dbc-4bdd-9dcd-7d18b0f87646	\N	2022-01-08 10:00:00.138	CC00000001	2022-01-08 09:00:00.229	CC00000001	2022-01-08 09:00:00.229	A
SOLRL2201000000000049	CC00000001	PW	0e0d5369-175c-456d-860b-4862c286ce1b	\N	2022-01-08 10:46:10.812	CC00000001	2022-01-08 09:46:10.902	CC00000001	2022-01-08 09:46:10.902	A
SOLRL2201000000000050	CC00000001	PW	d6b7c4e0-e2f7-4cd5-b5b0-cb4fb0e6edd1	\N	2022-01-08 23:46:26.669	CC00000001	2022-01-08 22:46:26.67	CC00000001	2022-01-08 22:48:33.126	I
SOLRL2201000000000051	CC00000001	PW	52389a02-49a4-48a2-83b2-f57357d5d954	\N	2022-01-09 00:18:36.863	CC00000001	2022-01-08 23:18:36.863	CC00000001	2022-01-08 23:24:18.776	I
SOLRL2201000000000052	CC00000003	PW	61df25f7-0dce-448d-a44e-48fc31750341	\N	2022-01-09 11:02:00.697	CC00000003	2022-01-09 10:02:00.698	CC00000003	2022-01-09 10:02:37.952	I
SOLRL2201000000000053	CC00000003	PW	7ae0693b-b2b3-425d-88b7-8f01641deae2	\N	2022-01-09 12:55:26.06	CC00000003	2022-01-09 11:55:26.061	CC00000003	2022-01-09 11:55:26.061	A
SOLRL2201000000000054	CC00000003	PW	472b5e8e-2aa3-43af-81c9-d56aa1edcd8f	\N	2022-01-09 13:44:46.735	CC00000003	2022-01-09 12:44:46.736	CC00000003	2022-01-09 12:45:16.851	I
SOLRL2201000000000055	CC00000003	PW	bff17209-b1f0-41cb-a958-0b508549ab15	\N	2022-01-09 15:08:05.001	CC00000003	2022-01-09 14:08:05.002	CC00000003	2022-01-09 14:08:30.611	I
\.


--
-- Data for Name: T_MS_RESERVATION; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_RESERVATION" ("RESV_ID", "RESV_INQUIRY_ID", "RESV_CHECK_IN_DATE_TIME", "RESV_REMARKS", "RESV_STATUS", "RESV_NO_OF_ADULTS", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "RESV_CHECK_OUT_DATE_TIME", "RESV_BRANCH_ID", "RESV_CANCELLATION_REASON", "RESV_NO_OF_CHILDREN") FROM stdin;
2	17	2021-12-28 07:03:11	Remarks two	1	3	2021-12-30 07:03:35	2021-12-30 07:03:38	CC00000001	\N	2021-12-30 07:04:01	1	\N	\N
4	41	2021-12-30 07:03:11	Reservation with new inquiry	1	2	2021-12-31 06:25:13.879	2021-12-31 06:25:13.879	CC00000001	CC00000001	2021-12-31 07:04:01	1	\N	\N
3	2	2021-12-20 07:03:11	Remarks three	0	3	2021-12-31 06:23:30.583	2021-12-31 06:54:00.682	CC00000001	CC00000001	2021-12-22 07:04:01	1	Reservation canceled	\N
36	68	2022-01-05 09:39:00	Rem	0	2	2022-01-05 21:40:23.965	2022-01-06 21:16:11.619	CC00000001	CC00000001	2022-01-07 21:39:00	1	Reservation canceled with all	\N
1	10	2021-12-30 07:03:11	Remarks one	0	4	2021-12-30 07:03:35	2022-01-06 22:46:17.698	CC00000001	CC00000001	2021-12-31 07:04:01	1	asasdasd	\N
9	38	2021-12-30 07:03:11	Reservation 38	1	2	2021-12-31 22:12:16.176	2021-12-31 23:04:47.608	CC00000001	CC00000001	2021-12-31 07:04:01	1	Reservation canceled with all	\N
19	59	2021-12-30 07:03:11	New res	1	3	2022-01-03 23:13:28.022	2022-01-03 23:13:28.022	CC00000001	CC00000001	2021-12-31 07:04:01	1	\N	\N
27	68	2022-01-07 07:03:11	2022 reservation	1	2	2022-01-05 08:06:37.224	2022-01-05 08:06:37.224	CC00000001	CC00000001	2022-01-08 07:04:01	1	\N	\N
39	83	2022-01-15 12:50:00	Rwm 1	2	2	2022-01-08 12:51:28.586	2022-01-08 14:35:45.14	CC00000001	CC00000001	2022-01-09 00:50:00	1	\N	1
41	93	2022-01-08 08:54:00		2	3	2022-01-08 20:55:00.932	2022-01-08 21:05:14.265	CC00000001	CC00000001	2022-01-09 08:54:00	1	\N	1
38	92	2022-01-08 07:03:11	2022 reservation 3	2	2	2022-01-08 08:16:57.777	2022-01-09 08:00:43.958	CC00000001	CC00000001	2022-01-09 07:04:01	1	\N	3
42	94	2022-01-10 12:54:00	Res 0001	2	3	2022-01-09 12:56:06.302	2022-01-09 12:58:09.419	CC00000001	CC00000001	2022-01-11 12:54:00	1	\N	1
43	95	2022-01-08 14:10:00	Rem123	2	3	2022-01-09 14:12:17.843	2022-01-09 14:13:48.985	CC00000001	CC00000001	2022-01-10 14:10:00	1	\N	1
\.


--
-- Data for Name: T_MS_ROLE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_ROLE" ("ROLE_ID", "ROLE_NAME", "ROLE_STATUS", "ROLE_DESCRIPTION") FROM stdin;
2	ROLE_GENERAL_MANAGER	1	General Manager
17	ROLE_ACCOUNTANT	1	Accountant
1	ROLE_ADMIN	1	Admin
20	ROLE_SECURITY_STAFF	1	Security Staff
5	ROLE_ROOM_SERVICE_STAFF	1	Room Service Staff
15	ROLE_RECEPTIONIST	1	Receptionist
18	ROLE_CASHIER	1	Cashier
4	ROLE_ROOM_SERVICE_MANAGER	1	Room Service Manager
13	ROLE_RESERVATION_MANAGER	1	Reservation Manager
7	ROLE_EXECUTIVE_CHEF	1	Executive Chef
10	ROLE_BAR_WAITRESS	1	Bar Waitress
19	ROLE_ADMIN_MANAGER	1	Admin Manager
9	ROLE_KITCHEN_HELPER	1	Kitchen Helper
14	ROLE_FRONT_OFFICE_STAFF	1	Front Office Staff
8	ROLE_CHEF	1	Chef
16	ROLE_FINANCE_MANAGER	1	Finance Manager
3	ROLE_ASSISTANT_MANAGER	1	Assistant Manager
6	ROLE_FOOD_MANAGER	1	Food Manager
21	ROLE_CLEARING_STAFF	1	Clearing Staff
11	ROLE_HR_MANAGER	1	Human Resources Manager
12	ROLE_HR_ASSISTANT	1	Human Resources Assistant
22	GENERAL	1	Common Features
\.


--
-- Data for Name: T_MS_ROLE_FUNCTION; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION" ("ROFU_ID", "ROFU_ROLE_ID", "ROFU_FUNC_ID", "ROFU_STATUS") FROM stdin;
56	14	INQ_UPDATE	1
57	14	INQ_DELETE	1
58	14	INQ_TRANSFER	1
59	14	INQ_CREATE	1
60	14	INQ_READ	1
61	14	RSV_UPDATE	1
62	14	RSV_DELETE	1
63	14	RSV_CANCEL	1
64	14	RSV_CREATE	1
65	14	RSV_READ	1
66	13	RSV_UPDATE	1
67	13	RSV_DELETE	1
68	13	RSV_CANCEL	1
69	13	RSV_CREATE	1
70	13	RSV_READ	1
71	13	ROOM_UPDATE	1
72	13	ROOM_DELETE	1
73	13	ROOM_CREATE	1
74	13	ROOM_READ	1
75	13	FCL_UPDATE	1
76	13	FCL_DELETE	1
77	13	FCL_CREATE	1
78	13	FCL_READ	1
79	13	ITM_READ	1
80	13	ITM_CREATE	1
81	13	ITM_UPDATE	1
82	13	ITM_DELETE	1
83	18	RSV_UPDATE	1
84	18	RSV_DELETE	1
1	1	CUS_CREATE	1
2	1	CUS_READ	1
3	1	INQ_CREATE	1
4	1	INQ_READ	1
7	1	CUS_UPDATE	1
8	1	CUS_DELETE	1
9	1	EMP_UPDATE	1
10	1	EMP_DELETE	1
11	1	SUP_CREATE	1
12	1	SUP_READ	1
14	1	MY_PROFILE_READ	1
13	1	ROOM_READ	1
17	1	DASHBOARD_READ	1
18	1	EMP_CREATE	1
19	1	RSV_READ	1
20	1	RSV_CREATE	1
21	1	RSV_UPDATE	1
22	1	RSV_DELETE	1
23	1	RSV_CANCEL	1
24	1	EMP_READ	1
25	1	USER_READ	1
26	1	USER_CREATE	1
27	1	USER_UPDATE	1
28	1	USER_DELETE	1
29	1	FCL_READ	1
30	1	FCL_CREATE	1
31	1	FCL_UPDATE	1
32	1	FCL_DELETE	1
33	1	ROOM_CREATE	1
34	1	ROOM_UPDATE	1
35	1	ROOM_DELETE	1
36	1	INQ_UPDATE	1
37	1	INQ_DELETE	1
38	1	INQ_TRANSFER	1
39	1	RPT_READ	1
41	1	RPT_GENERATE	1
42	1	RPT_REGENERATE	1
43	1	RPT_DELETE	1
44	1	ITM_READ	1
45	1	ITM_CREATE	1
46	1	ITM_UPDATE	1
47	1	ITM_DELETE	1
48	1	PAY_READ	1
49	1	PAY_CREATE	1
50	1	PAY_CANCEL	1
51	1	SAL_READ	1
52	1	ATT_READ	1
53	1	ATT_CREATE	1
54	1	LVE_READ	1
55	1	INV_READ	1
85	18	RSV_CANCEL	1
86	18	RSV_READ	1
87	18	PAY_CREATE	1
88	18	PAY_READ	1
89	18	PAY_CANCEL	1
90	18	PAY_INVOICE	1
91	18	INV_CREATE	1
92	18	INV_READ	1
93	17	ATT_READ	1
94	17	ATT_CREATE	1
95	17	LVE_CREATE	1
96	17	LVE_READ	1
97	17	SAL_CREATE	1
98	17	SAL_READ	1
99	17	INV_CREATE	1
100	17	INV_READ	1
101	14	CUS_UPDATE	1
102	14	CUS_DELETE	1
103	14	CUS_CREATE	1
104	14	CUS_READ	1
105	13	CUS_UPDATE	1
106	13	CUS_DELETE	1
107	13	CUS_CREATE	1
108	13	CUS_READ	1
109	11	EMP_CREATE	1
110	11	EMP_READ	1
111	11	EMP_UPDATE	1
112	11	EMP_DELETE	1
113	11	ATT_READ	1
114	11	ATT_CREATE	1
115	11	LVE_CREATE	1
116	11	LVE_READ	1
117	11	SAL_CREATE	1
118	11	SAL_READ	1
119	11	RPT_READ	1
120	11	RPT_GENERATE	1
121	11	RPT_REGENERATE	1
122	11	RPT_DELETE	1
123	13	RPT_READ	1
124	13	RPT_GENERATE	1
125	13	RPT_REGENERATE	1
126	13	RPT_DELETE	1
127	22	MY_PROFILE_READ	1
128	22	DASHBOARD_READ	1
129	2	RPT_READ	1
130	2	RPT_GENERATE	1
131	2	RPT_REGENERATE	1
132	2	RPT_DELETE	1
133	2	CUS_UPDATE	1
134	2	CUS_DELETE	1
135	2	EMP_UPDATE	1
136	2	EMP_DELETE	1
137	2	CUS_CREATE	1
138	2	EMP_CREATE	1
139	2	EMP_READ	1
140	2	USER_READ	1
141	2	USER_DELETE	1
142	2	USER_CREATE	1
143	2	USER_UPDATE	1
144	2	PAY_CREATE	1
145	2	PAY_READ	1
146	2	INV_CREATE	1
147	2	INV_READ	1
\.


--
-- Data for Name: T_MS_ROOM; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_ROOM" ("ROOM_ID", "ROOM_TYPE", "ROOM_PRICE", "ROOM_STATUS", "ROOM_BRANCH_ID", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "ROOM_DESCRIPTION", "ROOM_NO", "ROOM_BED_TYPE") FROM stdin;
5	RMTPJNT	10000.00	2	1	2021-12-25 23:24:27.357	2022-01-07 02:44:44.397	CC00000001	CC00000001	\N	23	RMBDTR
12	RMTPJNT	5200.00	2	1	2022-01-07 00:59:57.289	2022-01-08 08:16:57.967	CC00000001	CC00000001	\N	86	RMBDDO
10	RMTPJNT	5000.00	2	1	2022-01-05 20:52:41.786	2022-01-08 12:51:28.627	CC00000001	CC00000001	\N	35A	RMBDDO
11	RMTPJNT	9500.00	1	1	2022-01-06 20:51:39.634	2022-01-09 07:57:44.735	CC00000001	CC00000001	\N	20A	RMBDDO
9	RMTPSTD	12500.00	1	1	2022-01-01 20:22:25.877	2022-01-09 07:57:49.983	CC00000001	CC00000001	\N	12	RMBDTR
6	RMTPSTD	6666.00	2	1	2021-12-26 08:51:53.226	2022-01-09 12:56:06.319	CC00000001	CC00000001	\N	14	RMBDDO
2	RMTPJNT	25000.00	2	1	2021-12-25 21:55:09	2022-01-09 14:12:17.858	\N	CC00000001	\N	15B	RMBDDO
1	RMTPJNT	20000.00	5	1	2021-12-25 21:55:08	2022-01-02 19:00:12.065	\N	CC00000001	\N	A21	RMBDTR
4	RMTPSTD	10012.00	0	1	2021-12-25 22:59:33.146	2022-01-01 20:52:36.704	CC00000001	CC00000001	\N	52	RMBDDO
8	RMTPSTD	5656.00	2	1	2021-12-27 21:25:53.712	2022-01-02 18:41:26.051	CC00000001	CC00000001	\N	15D	RMBDTR
7	RMTPSTD	4444.00	2	1	2021-12-26 19:23:30.106	2022-01-03 23:13:28.293	CC00000001	CC00000001	\N	15A	RMBDTR
3	RMTPJNT	20000.00	4	2	2021-12-25 21:55:11	2022-01-07 01:23:48.585	\N	CC00000001	\N	31	RMBDTR
\.


--
-- Data for Name: T_MS_SALARY; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_SALARY" ("SALR_ID", "SALR_EMPLOYEE_CODE", "SALR_MONTH", "SALR_BASIC_SALARY", "SALR_ALLOWANCE", "SALR_DEDUCTION", "SALR_GROSS_SALARY", "SALR_NET_SALARY", "SALR_STATUS") FROM stdin;
\.


--
-- Data for Name: T_MS_USER; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_USER" ("USER_ID", "USER_PASSWORD", "USER_USERNAME", "USER_PARTY_CODE", "USER_STATUS") FROM stdin;
89	{bcrypt}$2a$10$Xc/7qSNb/dlo9VqPVL7ru.4cTF7yANaZC4mIqHKmVz6JvOKBg7mYC	Chamath	CC00000050	1
3	{bcrypt}$2a$10$gHIAmd62dsLyJByxfLX40O6lXfo49hyzZyoXTnB1q9LKEO4YDXPJa	sadeep	CC00000001	1
4	{bcrypt}$2a$10$M93gmfqCADXFLDrOj7Lm5.KD4.JpgAED.X8ORaX3qtiEp9hFSKOK.	randima	CC00000003	1
5	$2a$10$Fs5TTSSLcZ2jbeoGvD6tReRu/fnLqRCa3G5jjO8GhrvLdO21p55OS	chami	CC00000002	0
67	{bcrypt}$2a$10$IPijuXHHfQhiTNnXGGDMO.VfTBNU.wzZTsb.gXbjRT3clDR/JdAnG	admin	CC00000019	1
63	$2a$10$SoFHsJwnge2u4gogcQ789.oOd9d8Rign0yy1W06X4BEWi162GciOm	chamara2	CC00000004	0
73	{bcrypt}$2a$10$4sQQtYL48hy8jiPrzkm76e8.MKDHNFXjhTKq/.v84afNipair28ea	empone	CC00000012	1
74	{bcrypt}$2a$10$c87WFXxugmOS2yMAePHCpeltOz1b9D5wmoTtZGoOoBFKxdqkhZia6	ASCASC	CC00000008	1
75	{bcrypt}$2a$10$GPmpyhlHPkrUtzpE7J8oBustF3BLiYrYwoRTyl3DhvKlzFuiiKIt2	nilan	CC00000007	1
76	{bcrypt}$2a$10$0fDmIBu3hozqiMuMTq4vtOZbg7DrozT.W/nbTRLAh8Eb70472eSV2	hrm2	CC00000004	0
77	{bcrypt}$2a$10$V2tVrQBOsRiv1deYZcJh.uEJNCtMAsdZvQZW2YqjYC7ojuv/r0VjG	Chamari	CC00000002	1
78	{bcrypt}$2a$10$mEZOs25xFkCD9O27bpzVLuvVnIx/EqAtuIKApII4Qs0RuFATkQNMS	Danushka	CC00000044	1
79	{bcrypt}$2a$10$abFpQh8/7ph1HV4LCt6NreRVY97ppAlotuiF8O/1BF6c4HXCyQVGO	Harsha	CC00000045	1
80	{bcrypt}$2a$10$NWwc9ju7vmE3bgb/LmiNi.X8DRPILYPTKLfmdcHPWW9NBHithk6lS	Gayani	CC00000046	1
81	{bcrypt}$2a$10$GSTyNasnXmZuZsayPd7T.ugtAB3fH94CBpr5imcAgRYn9MwMeGlZG	Damith	CC00000047	1
84	{bcrypt}$2a$10$S4.6mqoTA2dbooQlSmmbLesm2bxaxRYToIAbaL.bsv2U9M9i33Toy	Nalaka	CC00000048	1
88	{bcrypt}$2a$10$mUUyFr1vq6rWBwF/WTwE9uhBpaDJrVurjAORbYGQLSR4MByNIklhe	Mahesh	CC00000049	1
\.


--
-- Data for Name: T_MS_USER_BRANCH; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_USER_BRANCH" ("USBR_ID", "USBR_USER_ID", "USBR_BRANCH_ID", "USBR_STATUS") FROM stdin;
3	5	1	0
5	67	1	1
6	67	2	1
20	89	2	0
22	89	1	1
7	63	1	0
8	63	2	0
9	73	2	1
10	74	2	1
13	77	2	1
18	84	2	0
23	84	1	1
16	80	2	0
24	80	1	1
14	78	2	0
25	78	1	1
26	4	1	1
2	3	2	0
21	3	3	0
1	3	1	1
11	75	1	0
27	75	2	1
12	76	2	0
15	79	2	1
17	81	2	1
19	88	2	1
\.


--
-- Data for Name: T_MS_USER_ROLE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_MS_USER_ROLE" ("USRL_USER_ID", "USRL_ROLE_ID", "USRL_ID", "USRL_STATUS") FROM stdin;
89	3	32	0
89	2	33	1
89	22	34	1
74	2	8	0
74	1	10	0
74	5	12	0
74	11	13	0
74	15	14	0
74	17	9	1
74	20	11	1
67	1	5	1
77	1	22	0
77	15	23	0
77	13	24	0
77	11	25	0
77	17	21	1
77	22	36	1
63	17	67	0
63	12	66	0
84	16	30	0
63	8	64	0
63	21	3	0
63	6	6	0
73	20	7	1
76	11	20	0
84	18	38	1
84	22	37	1
79	6	27	1
81	13	29	1
88	1	31	1
80	11	28	1
80	22	35	1
78	4	26	0
78	14	39	1
78	22	40	1
4	17	2	1
3	14	1	0
3	1	19	0
3	13	41	1
3	22	42	1
75	1	16	0
75	20	17	0
75	5	18	0
75	17	15	1
\.


--
-- Data for Name: T_PR_SYS_REF_NO_PARAM; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM" ("NMPR_SEQ_NO", "NMPR_YEAR", "NMPR_MONTH", "NMPR_SLOC_CODE", "NMPR_CLAS_CODE", "NMPR_PROD_CODE", "NMPR_ACCT_CODE", "NMPR_CUST_CD_PARAM", "CREATED_USER_CODE", "CREATED_DATE", "LAST_MOD_USER_CODE", "LAST_MOD_DATE", "NMPR_RCPT_NO_PARAM", "NMPR_PYVU_NO_PARAM", "VERSION", "NMPR_ROOM_NO_PARAM") FROM stdin;
NO002	#	#	#	#	#	#	52	1	1970-01-01 00:00:00	0	1970-01-01 00:00:00	2	7	0	1
NO001	#	#	#	#	#	#	52	1	1970-01-01 00:00:00	0	1970-01-01 00:00:00	2	7	0	1
\.


--
-- Data for Name: T_RF_BRANCH; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_RF_BRANCH" ("BRNH_ID", "BRNH_NAME", "BRNH_LOCATION", "BRNH_STATUS") FROM stdin;
1	Ratnapura	Ratnapura	1
2	Colombo	Colombo	1
3	Kandy	Kandy	1
\.


--
-- Data for Name: T_RF_COMMON_REFERENCE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE" ("CMRF_CODE", "CMRF_CMRT_CODE", "CMRF_DESCRIPTION", "CMRF_STATUS", "CMRF_STRING_VALUE", "CMRF_NUMBER_VALUES") FROM stdin;
CUSTM	PTYPE	Customer	1	\N	\N
EMPLY	PTYPE	Employee	1	\N	\N
CNMBL	CONTC	Mobile Number	1	\N	\N
CNEML	CONTC	Email Number	1	\N	\N
M	GENDR	Male	1	\N	\N
F	GENDR	Female	1	\N	\N
FCPOOL	FCLTP	Pool Facility	1	\N	\N
FCPARK	FCLTP	Parking Facility	1	\N	\N
UOMHR	UOFMS	No of Hours	1	\N	\N
RMBDSI	RMBDT	Single Bed Type	1	\N	\N
RMBDDO	RMBDT	Double Bed Type	1	\N	\N
RMBDTR	RMBDT	Triple Bed Type	1	\N	\N
UOMHLS	UOFMS	Hard Liquor Shots	1	\N	\N
ITMHLQ	ITMTP	Hard Liquor	1	\N	\N
PTCASH	PAYTP	Cash Payment	1	\N	\N
PTBDEP	PAYTP	Bank Deposit	1	\N	\N
PTONLN	PAYTP	Online Transfer	1	\N	\N
FCBAR	FCLTP	Bar Facility	1	\N	\N
FCOTDR	FCLTP	Outdoor Facility	1	\N	\N
FCINDR	FCLTP	Indoor Facility	1	\N	\N
FCHELT	FCLTP	Health Realted Facility	1	\N	\N
UOMKG	UOFMS	KG	1	Kilo Grams	\N
UOMBTL	UOFMS	Bottles	1	\N	\N
UOMLIT	UOFMS	Liters	1	Liters	\N
UOMPCS	UOFMS	Peices	1	\N	\N
UOMBOX	UOFMS	Boxes	1	\N	\N
UOMBAG	UOFMS	Bags	1	\N	\N
UOMKIT	UOFMS	Kits	1	\N	\N
UOMCTN	UOFMS	Carton	1	\N	\N
UOMSHT	UOFMS	Sheets	1	\N	\N
ITMBER	ITMTP	Beer	1	\N	\N
ITMWNE	ITMTP	Wine	1	\N	\N
ITMCIG	ITMTP	Cigarette	1	\N	\N
ITMCKT	ITMTP	Cocktail	1	\N	\N
ITMBVT	ITMTP	Beverage Tubs	1	\N	\N
ITMSHK	ITMTP	Shakers	1	\N	\N
ITMSPT	ITMTP	Serving Platters	1	\N	\N
ITMHST	ITMTP	Hotel Sheets	1	\N	\N
ITMBDN	ITMTP	Hotel Bedding	1	\N	\N
RMTPDEL	ROMTP	Deluxe Room	1	\N	\N
RMTPJNT	ROMTP	Joint Room	1	\N	\N
RMTPSTD	ROMTP	Standard Room	1	\N	\N
\.


--
-- Data for Name: T_RF_COMMON_REFERENCE_TYPE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE_TYPE" ("CMRT_CODE", "CMRT_DESCRIPTION", "CMRT_STATUS") FROM stdin;
PTYPE	Party Types	1
CONTC	Contact Types	1
GENDR	Gender Types	1
ROMTP	Room Types	1
RMCAT	Room Category	1
FCLTP	Facility Types	1
UOFMS	Units of Measurement	1
RMBDT	Room Bed Types	1
ITMTP	Item Types	1
PAYTP	Payment Types	1
\.


--
-- Data for Name: T_RF_REPORT_TYPE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_RF_REPORT_TYPE" ("RPTP_ID", "RPTP_CODE", "RPTP_DISPLAY_NAME", "RPTP_ICON", "RPTP_COLOR", "RPTP_STATUS", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE") FROM stdin;
2	RTRESDT	Reservation Detailed Report	feather icon-file-text	#BCB4EE	1	2022-01-02 23:07:57	2022-01-02 23:08:00	\N	\N
7	RTLIQDT	Liquor & Bar Detailed Report	feather icon-file-text	#BCB4EE	1	2022-01-03 19:00:53	2022-01-03 19:00:55	\N	\N
3	RTATTDT	Employee Attendance Detailed Report	feather icon-file-text	#BCB4EE	1	2022-01-02 23:07:57	2022-01-02 23:08:00	\N	\N
6	RTEXPDT	Expenditure Details Report	feather icon-file-text	#BCB4EE	1	2022-01-03 18:59:54	2022-01-03 18:59:55	\N	\N
1	RTINQDT	Inquiry Detailed Report	feather icon-file-text	#BCB4EE	1	2022-01-02 23:07:57	2022-01-02 23:08:00	\N	\N
4	RTDALIC	Income Detailed Report	feather icon-file-text	#BCB4EE	1	2022-01-02 23:07:57	2022-01-02 23:08:00	\N	\N
5	RTMONIC	Invoice Wise Income Detailed Report	feather icon-file-text	#BCB4EE	1	2022-01-02 23:07:57	2022-01-02 23:08:00	\N	\N
\.


--
-- Data for Name: T_TR_FACILITY_RESERVATION; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION" ("FARE_ID", "FARE_FACILITY_ID", "FARE_RESERVATION_ID", "FARE_QUANTITY", "FARE_STATUS", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "FARE_BRANCH_ID") FROM stdin;
12	2	9	2	1	2021-12-31 22:12:17.61	2021-12-31 23:04:48.202	CC00000001	CC00000001	1
11	5	9	1	1	2021-12-31 22:12:17.255	2021-12-31 23:04:48.201	CC00000001	CC00000001	1
23	5	19	2	1	2022-01-03 23:13:29.233	2022-01-03 23:13:29.233	CC00000001	CC00000001	1
24	2	19	8	1	2022-01-03 23:13:29.858	2022-01-03 23:13:29.858	CC00000001	CC00000001	1
39	7	36	2	0	2022-01-05 21:40:23.981	2022-01-06 21:16:12.264	CC00000001	CC00000001	1
31	7	27	2	0	2022-01-05 08:06:38.409	2022-01-07 02:17:49.884	CC00000001	CC00000001	1
40	7	27	2	0	2022-01-07 02:17:49.953	2022-01-07 02:19:24.631	CC00000001	CC00000001	1
41	7	27	2	0	2022-01-07 02:19:24.652	2022-01-07 02:44:44.379	CC00000001	CC00000001	1
42	7	27	2	1	2022-01-07 02:44:44.388	2022-01-07 02:44:44.388	CC00000001	CC00000001	1
43	8	38	2	1	2022-01-08 08:16:58.611	2022-01-08 08:16:58.611	CC00000001	CC00000001	1
44	8	39	3	1	2022-01-08 12:51:28.689	2022-01-08 12:51:28.689	CC00000001	CC00000001	1
46	7	41	3	0	2022-01-08 20:55:00.946	2022-01-08 21:01:47.106	CC00000001	CC00000001	1
47	7	41	3	0	2022-01-08 21:01:47.117	2022-01-08 21:02:02.349	CC00000001	CC00000001	1
48	7	41	3	1	2022-01-08 21:02:02.367	2022-01-08 21:02:02.367	CC00000001	CC00000001	1
49	7	42	3	1	2022-01-09 12:56:06.412	2022-01-09 12:56:06.412	CC00000001	CC00000001	1
50	8	43	5	1	2022-01-09 14:12:17.866	2022-01-09 14:12:17.866	CC00000001	CC00000001	1
\.


--
-- Data for Name: T_TR_INVOICE; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_TR_INVOICE" ("INVC_ID", "INVC_DESCRIPTION", "INVC_GROSS_AMOUNT", "INVC_NET_AMOUNT", "INVC_STATUS", "INVC_BRANCH_ID", "INVC_DISCOUNT_AMOUNT", "INVC_TAX_AMOUNT", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "INVC_NUMBER", "INVC_RESERVATION_ID") FROM stdin;
10	Invoice for the reservation 27	29500.00	26550.00	1	1	5900.00	2950.00	2022-01-06 22:46:01.146	2022-01-06 22:46:01.146	CC00000001	CC00000001	IN0000000001	27
14	10,327.00	10900.00	10327.00	1	1	900.00	327.00	2022-01-08 18:48:47.338	2022-01-08 18:48:47.338	CC00000001	CC00000001	IV0000000003	39
15	Res done thanks	22623.00	21265.62	1	1	2262.30	904.92	2022-01-08 21:05:43.506	2022-01-08 21:05:43.506	CC00000001	CC00000001	IV0000000004	41
16		6320.00	6320.00	1	1	0.00	0.00	2022-01-09 08:01:58.771	2022-01-09 08:01:58.771	CC00000001	CC00000001	IV0000000005	38
17	Inv 001	12466.00	11593.38	1	1	1246.60	373.98	2022-01-09 12:59:01.85	2022-01-09 12:59:01.85	CC00000001	CC00000001	IV0000000006	42
18		27020.00	27020.00	1	1	0.00	0.00	2022-01-09 14:15:11.928	2022-01-09 14:15:11.928	CC00000001	CC00000001	IV0000000007	43
\.


--
-- Data for Name: T_TR_INVOICE_DET; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_TR_INVOICE_DET" ("INDT_ID", "INDT_INVOICE_ID", "INDT_RESERVATION_ID", "INDT_ROOM_RESERVATION_ID", "INDT_FACILITY_RESERVATION_ID", "INDT_ITEM_RESERVATION_ID", "INDT_RESERVED_QUANTITY", "INDT_UNIT_PRICE", "INDT_STATUS", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "INDT_BRANCH_ID", "INDT_AMOUNT") FROM stdin;
15	10	27	23	\N	\N	1	10000.00	1	2022-01-06 22:46:05.093	2022-01-06 22:46:05.093	CC00000001	CC00000001	1	10000.00
16	10	27	\N	31	\N	2	1500.00	1	2022-01-06 22:46:05.183	2022-01-06 22:46:05.183	CC00000001	CC00000001	1	3000.00
17	10	27	\N	\N	9	1	6500.00	1	2022-01-06 22:46:05.267	2022-01-06 22:46:05.267	CC00000001	CC00000001	1	6500.00
27	14	39	36	\N	\N	1	5000.00	1	2022-01-08 18:48:47.474	2022-01-08 18:48:47.474	CC00000001	CC00000001	1	5000.00
28	14	39	\N	44	\N	3	300.00	1	2022-01-08 18:48:47.497	2022-01-08 18:48:47.497	CC00000001	CC00000001	1	900.00
29	14	39	\N	\N	23	1	5000.00	1	2022-01-08 18:48:47.498	2022-01-08 18:48:47.498	CC00000001	CC00000001	1	5000.00
30	15	41	40	\N	\N	1	123.00	1	2022-01-08 21:05:43.563	2022-01-08 21:05:43.563	CC00000001	CC00000001	1	123.00
31	15	41	\N	48	\N	3	1500.00	1	2022-01-08 21:05:43.568	2022-01-08 21:05:43.568	CC00000001	CC00000001	1	4500.00
32	15	41	\N	\N	26	1	5000.00	1	2022-01-08 21:05:43.569	2022-01-08 21:05:43.569	CC00000001	CC00000001	1	5000.00
33	15	41	\N	\N	27	2	6500.00	1	2022-01-08 21:05:43.57	2022-01-08 21:05:43.57	CC00000001	CC00000001	1	13000.00
34	16	38	35	\N	\N	1	5200.00	1	2022-01-09 08:01:58.828	2022-01-09 08:01:58.828	CC00000001	CC00000001	1	5200.00
35	16	38	\N	43	\N	2	300.00	1	2022-01-09 08:01:58.83	2022-01-09 08:01:58.83	CC00000001	CC00000001	1	600.00
36	16	38	\N	\N	22	2	260.00	1	2022-01-09 08:01:58.841	2022-01-09 08:01:58.841	CC00000001	CC00000001	1	520.00
37	17	42	41	\N	\N	1	6666.00	1	2022-01-09 12:59:01.891	2022-01-09 12:59:01.891	CC00000001	CC00000001	1	6666.00
38	17	42	\N	49	\N	3	1500.00	1	2022-01-09 12:59:01.893	2022-01-09 12:59:01.893	CC00000001	CC00000001	1	4500.00
39	17	42	\N	\N	28	5	260.00	1	2022-01-09 12:59:01.894	2022-01-09 12:59:01.894	CC00000001	CC00000001	1	1300.00
40	18	43	42	\N	\N	1	25000.00	1	2022-01-09 14:15:11.949	2022-01-09 14:15:11.949	CC00000001	CC00000001	1	25000.00
41	18	43	\N	50	\N	5	300.00	1	2022-01-09 14:15:11.95	2022-01-09 14:15:11.95	CC00000001	CC00000001	1	1500.00
42	18	43	\N	\N	29	2	260.00	1	2022-01-09 14:15:11.951	2022-01-09 14:15:11.951	CC00000001	CC00000001	1	520.00
\.


--
-- Data for Name: T_TR_ITEM_RESERVATION; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION" ("ITRS_ID", "ITRS_ITEM_ID", "ITRS_RESERVATION_ID", "ITRS_QUANTITY", "ITRS_STATUS", "ITRS_BRANCH_ID", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE") FROM stdin;
2	1	19	5.00	1	1	2022-01-03 23:13:30.215	2022-01-03 23:13:30.215	CC00000001	CC00000001
17	4	36	3.00	0	1	2022-01-05 21:40:23.985	2022-01-06 21:16:12.264	CC00000001	CC00000001
18	2	36	2.00	0	1	2022-01-05 21:40:23.987	2022-01-06 21:16:12.265	CC00000001	CC00000001
9	10	27	1.00	0	1	2022-01-05 08:06:38.792	2022-01-07 02:17:49.887	CC00000001	CC00000001
19	10	27	1.00	0	1	2022-01-07 02:17:49.959	2022-01-07 02:19:24.631	CC00000001	CC00000001
20	10	27	1.00	0	1	2022-01-07 02:19:24.656	2022-01-07 02:44:44.379	CC00000001	CC00000001
21	10	27	1.00	1	1	2022-01-07 02:44:44.396	2022-01-07 02:44:44.396	CC00000001	CC00000001
22	2	38	2.00	1	1	2022-01-08 08:16:58.982	2022-01-08 08:16:58.982	CC00000001	CC00000001
23	11	39	1.00	1	1	2022-01-08 12:51:28.718	2022-01-08 12:51:28.718	CC00000001	CC00000001
25	11	41	1.00	0	1	2022-01-08 21:01:47.128	2022-01-08 21:02:02.349	CC00000001	CC00000001
26	11	41	1.00	1	1	2022-01-08 21:02:02.37	2022-01-08 21:02:02.37	CC00000001	CC00000001
27	8	41	2.00	1	1	2022-01-08 21:02:02.374	2022-01-08 21:02:02.374	CC00000001	CC00000001
28	2	42	5.00	1	1	2022-01-09 12:56:06.428	2022-01-09 12:56:06.428	CC00000001	CC00000001
29	2	43	2.00	1	1	2022-01-09 14:12:17.869	2022-01-09 14:12:17.869	CC00000001	CC00000001
\.


--
-- Data for Name: T_TR_PAYMENT; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_TR_PAYMENT" ("PAYT_ID", "PAYT_RESERVATION_ID", "PAYT_DESCRIPTION", "PAYT_TYPE", "PAYT_AMOUNT", "PAYT_STATUS", "PAYT_BRANCH_ID", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "PAYT_CANCEL_REASON") FROM stdin;
6	27	2nd payment	PTCASH	1000.00	0	1	2022-01-06 05:32:17.807	2022-01-06 05:34:39.1	CC00000001	CC00000001	Wrong payment
7	27	3nd payment	PTCASH	24500.00	1	1	2022-01-06 21:58:42.393	2022-01-06 21:58:42.393	CC00000001	CC00000001	\N
5	27	\N	PTCASH	5000.00	0	1	2022-01-05 08:06:39.093	2022-01-07 14:20:56.746	CC00000001	CC00000001	yhh6h6h6
8	38	\N	PTCASH	6000.00	1	1	2022-01-08 08:17:00.103	2022-01-08 08:17:00.103	CC00000001	CC00000001	\N
9	39	Init Payment	PTCASH	900.00	1	1	2022-01-08 12:52:49.876	2022-01-08 12:52:49.876	CC00000001	CC00000001	\N
10	39	Second Pay	PTBDEP	10000.00	1	1	2022-01-08 12:54:41.579	2022-01-08 12:54:41.579	CC00000001	CC00000001	\N
11	41	\N	PTCASH	2000.00	1	1	2022-01-08 20:55:00.979	2022-01-08 20:55:00.979	CC00000001	CC00000001	\N
12	41	Pay 1	PTCASH	15000.00	1	1	2022-01-08 21:03:02.031	2022-01-08 21:03:02.031	CC00000001	CC00000001	\N
13	41	Pay 2	PTCASH	2000.00	1	1	2022-01-08 21:03:56.095	2022-01-08 21:03:56.095	CC00000001	CC00000001	\N
14	41	Pay 3	PTCASH	1500.00	1	1	2022-01-08 21:04:19.683	2022-01-08 21:04:19.683	CC00000001	CC00000001	\N
15	41	Pay 3	PTCASH	2123.00	1	1	2022-01-08 21:05:05.203	2022-01-08 21:05:05.203	CC00000001	CC00000001	\N
16	38		PTONLN	320.00	1	1	2022-01-09 07:59:45.075	2022-01-09 07:59:45.075	CC00000001	CC00000001	\N
17	42	\N	PTCASH	1500.00	1	1	2022-01-09 12:56:06.484	2022-01-09 12:56:06.484	CC00000001	CC00000001	\N
18	42	Final Payment	PTCASH	10966.00	1	1	2022-01-09 12:57:40.956	2022-01-09 12:57:40.956	CC00000001	CC00000001	\N
19	43	\N	PTCASH	7020.00	1	1	2022-01-09 14:12:17.88	2022-01-09 14:12:17.88	CC00000001	CC00000001	\N
20	43	Payment 2	PTCASH	20000.00	1	1	2022-01-09 14:13:34.849	2022-01-09 14:13:34.849	CC00000001	CC00000001	\N
\.


--
-- Data for Name: T_TR_REPORT_HISTORY; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_TR_REPORT_HISTORY" ("RPHT_ID", "RPHT_REPORT_TYPE", "RPHT_STATUS", "RPHT_FROM_DATE", "RPHT_TO_DATE", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "RPHT_BRANH_ID") FROM stdin;
63	2	1	2022-01-04 00:00:00	2022-01-04 23:59:59	2022-01-04 08:14:38.289	2022-01-04 08:14:38.289	CC00000001	CC00000001	1
3	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 00:09:59.075	2022-01-03 00:09:59.075	CC00000001	CC00000001	1
4	1	0	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 00:19:49.955	2022-01-03 07:04:57.607	CC00000001	CC00000001	1
7	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 08:09:49.405	2022-01-03 08:09:49.405	CC00000001	CC00000001	1
8	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 14:43:53.436	2022-01-03 14:43:53.436	CC00000001	CC00000001	1
30	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 16:24:27.564	2022-01-03 16:24:27.564	CC00000001	CC00000001	1
31	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 19:59:48.206	2022-01-03 19:59:48.206	CC00000001	CC00000001	1
32	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 20:01:44.309	2022-01-03 20:01:44.309	CC00000001	CC00000001	1
33	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:17:08.308	2022-01-03 22:17:08.308	CC00000001	CC00000001	1
34	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:17:38.829	2022-01-03 22:17:38.829	CC00000001	CC00000001	1
35	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:18:12.204	2022-01-03 22:18:12.204	CC00000001	CC00000001	1
36	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:19:04.195	2022-01-03 22:19:04.195	CC00000001	CC00000001	1
37	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:19:33.717	2022-01-03 22:19:33.717	CC00000001	CC00000001	1
38	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:44:28.438	2022-01-03 22:44:28.438	CC00000001	CC00000001	1
39	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:44:50.527	2022-01-03 22:44:50.527	CC00000001	CC00000001	1
41	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:52:20.587	2022-01-03 22:52:20.587	CC00000001	CC00000001	1
42	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:56:08.005	2022-01-03 22:56:08.005	CC00000001	CC00000001	1
43	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 23:03:32.273	2022-01-03 23:03:32.273	CC00000001	CC00000001	1
44	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 23:07:13.418	2022-01-03 23:07:13.418	CC00000001	CC00000001	1
45	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 23:18:07.844	2022-01-03 23:18:07.844	CC00000001	CC00000001	1
46	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 23:21:19.612	2022-01-03 23:21:19.612	CC00000001	CC00000001	1
47	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 23:26:08.419	2022-01-03 23:26:08.419	CC00000001	CC00000001	1
48	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 23:28:43.63	2022-01-03 23:28:43.63	CC00000001	CC00000001	1
49	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 23:29:58.298	2022-01-03 23:29:58.298	CC00000001	CC00000001	1
40	1	0	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-03 22:51:25.271	2022-01-03 23:54:40.269	CC00000001	CC00000001	1
50	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-04 01:12:27.519	2022-01-04 01:12:27.519	CC00000001	CC00000001	1
51	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-04 01:13:34.119	2022-01-04 01:13:34.119	CC00000001	CC00000001	1
52	1	1	2021-12-01 00:00:00	2022-01-01 23:59:59	2022-01-04 01:14:14.704	2022-01-04 01:14:14.704	CC00000001	CC00000001	1
53	1	1	2021-12-01 00:00:00	2022-01-30 23:59:59	2022-01-04 01:22:50.123	2022-01-04 01:22:50.123	CC00000001	CC00000001	1
54	1	1	2021-12-01 00:00:00	2022-01-31 23:59:59	2022-01-04 01:25:26.417	2022-01-04 01:25:26.417	CC00000001	CC00000001	1
55	1	1	2022-01-03 00:00:00	2022-02-04 23:59:59	2022-01-04 01:27:30.869	2022-01-04 01:27:30.869	CC00000001	CC00000001	1
56	1	1	2022-01-03 00:00:00	2022-02-04 23:59:59	2022-01-04 01:28:03.967	2022-01-04 01:28:03.967	CC00000001	CC00000001	1
57	1	1	2022-01-03 00:00:00	2022-02-04 23:59:59	2022-01-04 01:40:31.291	2022-01-04 01:40:31.291	CC00000001	CC00000001	1
58	2	1	2021-12-01 00:00:00	2022-01-04 23:59:59	2022-01-04 07:11:31.641	2022-01-04 07:11:31.641	CC00000001	CC00000001	1
59	1	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 07:21:07.412	2022-01-04 07:21:07.412	CC00000001	CC00000001	1
60	2	1	2021-12-01 00:00:00	2022-01-04 23:59:59	2022-01-04 07:35:13.24	2022-01-04 07:35:13.24	CC00000001	CC00000001	1
61	2	1	2021-12-01 00:00:00	2022-01-04 23:59:59	2022-01-04 07:38:33.007	2022-01-04 07:38:33.007	CC00000001	CC00000001	1
62	2	1	2021-12-01 00:00:00	2022-01-04 23:59:59	2022-01-04 07:43:15.592	2022-01-04 07:43:15.592	CC00000001	CC00000001	1
64	2	1	2022-01-04 00:00:00	2022-01-04 23:59:59	2022-01-04 08:15:04.783	2022-01-04 08:15:04.783	CC00000001	CC00000001	1
65	2	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 08:15:32.794	2022-01-04 08:15:32.794	CC00000001	CC00000001	1
66	2	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 08:16:02.068	2022-01-04 08:16:02.068	CC00000001	CC00000001	1
67	1	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 08:29:18.874	2022-01-04 08:29:18.874	CC00000001	CC00000001	1
68	2	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 08:29:48.236	2022-01-04 08:29:48.236	CC00000001	CC00000001	1
69	2	1	2021-12-01 00:00:00	2022-01-04 23:59:59	2022-01-04 08:29:59.316	2022-01-04 08:29:59.316	CC00000001	CC00000001	1
70	1	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 10:25:34.409	2022-01-04 10:25:34.409	CC00000001	CC00000001	1
71	2	1	2021-12-01 00:00:00	2022-01-04 23:59:59	2022-01-04 10:26:03.381	2022-01-04 10:26:03.381	CC00000001	CC00000001	1
72	2	1	2022-01-04 00:00:00	2022-01-04 23:59:59	2022-01-04 12:18:08.462	2022-01-04 12:18:08.462	CC00000001	CC00000001	1
73	2	1	2022-01-04 00:00:00	2022-01-04 23:59:59	2022-01-04 18:27:23.114	2022-01-04 18:27:23.114	CC00000001	CC00000001	1
74	1	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 18:28:08.132	2022-01-04 18:28:08.132	CC00000001	CC00000001	1
75	1	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-04 18:28:34.728	2022-01-04 18:28:34.728	CC00000001	CC00000001	1
76	2	1	2021-12-01 00:00:00	2022-01-04 23:59:59	2022-01-04 21:12:01.312	2022-01-04 21:12:01.312	CC00000001	CC00000001	1
77	2	1	2021-11-28 00:00:00	2022-01-27 23:59:59	2022-01-04 21:13:37.064	2022-01-04 21:13:37.064	CC00000001	CC00000001	1
78	1	1	2022-01-03 00:00:00	2022-01-03 23:59:59	2022-01-05 17:55:53.425	2022-01-05 17:55:53.425	CC00000001	CC00000001	1
79	2	1	2022-01-04 00:00:00	2022-01-04 23:59:59	2022-01-05 23:20:46.279	2022-01-05 23:20:46.279	CC00000001	CC00000001	1
80	2	1	2022-01-01 00:00:00	2022-01-05 23:59:59	2022-01-06 08:49:44.609	2022-01-06 08:49:44.609	CC00000001	CC00000001	1
81	1	1	2021-12-01 00:00:00	2022-01-31 23:59:59	2022-01-06 12:02:56.579	2022-01-06 12:02:56.579	CC00000001	CC00000001	1
82	2	1	2021-12-29 00:00:00	2022-01-05 23:59:59	2022-01-06 14:33:03.244	2022-01-06 14:33:03.244	CC00000001	CC00000001	1
83	2	1	2021-12-01 00:00:00	2021-12-31 23:59:59	2022-01-06 14:34:06.918	2022-01-06 14:34:06.918	CC00000001	CC00000001	1
84	1	1	2021-12-01 00:00:00	2021-12-31 23:59:59	2022-01-06 14:35:10.865	2022-01-06 14:35:10.865	CC00000001	CC00000001	1
85	3	1	2021-12-27 00:00:00	2022-01-05 23:59:59	2022-01-06 22:22:56.79	2022-01-06 22:22:56.79	CC00000001	CC00000001	1
86	5	1	2022-01-04 00:00:00	2022-01-06 23:59:59	2022-01-07 13:03:37.057	2022-01-07 13:03:37.057	CC00000001	CC00000001	1
87	4	1	2022-01-02 00:00:00	2022-01-08 23:59:59	2022-01-07 15:47:33.804	2022-01-07 15:47:33.804	CC00000001	CC00000001	1
88	2	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 14:18:27.438	2022-01-08 14:18:27.438	CC00000001	CC00000001	1
89	4	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 15:48:10.107	2022-01-08 15:48:10.107	CC00000001	CC00000001	1
90	5	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 16:02:32.657	2022-01-08 16:02:32.657	CC00000001	CC00000001	1
91	5	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 16:07:22.47	2022-01-08 16:07:22.47	CC00000001	CC00000001	1
92	4	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 16:13:00.417	2022-01-08 16:13:00.417	CC00000001	CC00000001	1
93	4	1	2021-12-01 00:00:00	2022-02-05 23:59:59	2022-01-08 16:13:27.124	2022-01-08 16:13:27.124	CC00000001	CC00000001	1
94	2	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 18:22:59.207	2022-01-08 18:22:59.207	CC00000001	CC00000001	1
95	5	1	2021-12-01 00:00:00	2022-02-07 23:59:59	2022-01-08 18:50:54.837	2022-01-08 18:50:54.837	CC00000001	CC00000001	1
96	2	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 21:46:52.293	2022-01-08 21:46:52.293	CC00000001	CC00000001	1
97	3	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 23:20:41.769	2022-01-08 23:20:41.769	CC00000001	CC00000001	1
98	3	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-08 23:24:38.242	2022-01-08 23:24:38.242	CC00000001	CC00000001	1
99	2	1	2022-01-01 00:00:00	2022-01-09 23:59:59	2022-01-09 13:11:44.722	2022-01-09 13:11:44.722	CC00000001	CC00000001	1
100	4	1	2021-12-31 00:00:00	2022-01-09 23:59:59	2022-01-09 13:15:07.073	2022-01-09 13:15:07.073	CC00000001	CC00000001	1
101	5	1	2022-01-30 00:00:00	2022-01-09 23:59:59	2022-01-09 13:16:34.563	2022-01-09 13:16:34.563	CC00000001	CC00000001	1
102	5	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-09 13:17:13.801	2022-01-09 13:17:13.801	CC00000001	CC00000001	1
103	3	1	2022-01-01 00:00:00	2022-01-09 23:59:59	2022-01-09 13:33:48.26	2022-01-09 13:33:48.26	CC00000001	CC00000001	1
104	2	1	2021-12-26 00:00:00	2022-01-09 23:59:59	2022-01-09 14:21:23.407	2022-01-09 14:21:23.407	CC00000001	CC00000001	1
105	1	1	2021-12-30 00:00:00	2022-01-09 23:59:59	2022-01-09 14:22:22.575	2022-01-09 14:22:22.575	CC00000001	CC00000001	1
106	4	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-09 14:23:08.315	2022-01-09 14:23:08.315	CC00000001	CC00000001	1
107	5	1	2021-12-01 00:00:00	2022-01-09 23:59:59	2022-01-09 14:23:44.554	2022-01-09 14:23:44.554	CC00000001	CC00000001	1
108	3	1	2022-01-01 00:00:00	2022-01-09 23:59:59	2022-01-09 14:24:22.803	2022-01-09 14:24:22.803	CC00000001	CC00000001	1
\.


--
-- Data for Name: T_TR_ROOM_RESERVATION; Type: TABLE DATA; Schema: LAKDERANA_BASE; Owner: postgres
--

COPY "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION" ("RORE_ID", "RORE_ROOM_ID", "RORE_RESERVATION_ID", "RORE_STATUS", "CREATED_DATE", "LAST_MOD_DATE", "CREATED_USER_CODE", "LAST_MOD_USER_CODE", "RORE_BRANCH_ID") FROM stdin;
8	8	9	1	2021-12-31 22:12:16.919	2021-12-31 23:04:48.195	CC00000001	CC00000001	1
15	7	19	1	2022-01-03 23:13:28.753	2022-01-03 23:13:28.753	CC00000001	CC00000001	1
31	10	36	0	2022-01-05 21:40:23.971	2022-01-06 21:16:12.263	CC00000001	CC00000001	1
23	5	27	0	2022-01-05 08:06:37.96	2022-01-07 02:17:49.884	CC00000001	CC00000001	1
32	5	27	0	2022-01-07 02:17:49.925	2022-01-07 02:19:24.631	CC00000001	CC00000001	1
33	5	27	0	2022-01-07 02:19:24.648	2022-01-07 02:44:44.379	CC00000001	CC00000001	1
34	5	27	1	2022-01-07 02:44:44.384	2022-01-07 02:44:44.384	CC00000001	CC00000001	1
35	12	38	1	2022-01-08 08:16:58.333	2022-01-08 08:16:58.333	CC00000001	CC00000001	1
36	10	39	1	2022-01-08 12:51:28.657	2022-01-08 12:51:28.657	CC00000001	CC00000001	1
38	9	41	0	2022-01-08 20:55:00.941	2022-01-08 21:01:47.106	CC00000001	CC00000001	1
39	9	41	0	2022-01-08 21:01:47.112	2022-01-08 21:02:02.349	CC00000001	CC00000001	1
40	9	41	1	2022-01-08 21:02:02.354	2022-01-08 21:02:02.354	CC00000001	CC00000001	1
41	6	42	1	2022-01-09 12:56:06.383	2022-01-09 12:56:06.383	CC00000001	CC00000001	1
42	2	43	1	2022-01-09 14:12:17.862	2022-01-09 14:12:17.862	CC00000001	CC00000001	1
\.


--
-- Data for Name: T_RF_COMMON_REFERENCE_TYPE; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."T_RF_COMMON_REFERENCE_TYPE" ("CMRT_CODE", "CMRT_DESCRIPTION", "CMRT_STATUS") FROM stdin;
\.


--
-- Name: S_CM_MS_PARTY_TOKEN; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."S_CM_MS_PARTY_TOKEN"', 55, true);


--
-- Name: T_MS_ATTENDANCE_ATTN_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_ATTENDANCE_ATTN_ID_seq"', 6, true);


--
-- Name: T_MS_FACILITY_FCLT_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_FACILITY_FCLT_ID_seq"', 9, true);


--
-- Name: T_MS_INQUIRY_INQR_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_INQUIRY_INQR_ID_seq"', 95, true);


--
-- Name: T_MS_ITEM_ITEM_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_ITEM_ITEM_ID_seq"', 12, true);


--
-- Name: T_MS_LEAVE_LEAV_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_LEAVE_LEAV_ID_seq"', 1, false);


--
-- Name: T_MS_PARTY_CONTACT_PTCN_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_PARTY_CONTACT_PTCN_ID_seq"', 42, true);


--
-- Name: T_MS_RESERVATION_RESV_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_RESERVATION_RESV_ID_seq"', 43, true);


--
-- Name: T_MS_ROLE_FUNCTION_ROFU_Id_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_ROLE_FUNCTION_ROFU_Id_seq"', 6, true);


--
-- Name: T_MS_ROLE_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_ROLE_ID_seq"', 1, true);


--
-- Name: T_MS_ROOM_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_ROOM_ID_seq"', 12, true);


--
-- Name: T_MS_ROOM_ROOM_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_ROOM_ROOM_ID_seq"', 1, false);


--
-- Name: T_MS_SALARY_SALR_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_SALARY_SALR_ID_seq"', 1, false);


--
-- Name: T_MS_USER_BRANCH_USBR_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_USER_BRANCH_USBR_ID_seq"', 6, true);


--
-- Name: T_MS_USER_USER_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_MS_USER_USER_ID_seq"', 89, true);


--
-- Name: T_RF_BRANCH_BRNH_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_RF_BRANCH_BRNH_ID_seq"', 27, true);


--
-- Name: T_RF_REPORT_TYPE_RPTP_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_RF_REPORT_TYPE_RPTP_ID_seq"', 1, true);


--
-- Name: T_RF_USER_ROLES_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_RF_USER_ROLES_ID_seq"', 42, true);


--
-- Name: T_TR_FACILITY_RESERVATION_FARE_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION_FARE_ID_seq"', 50, true);


--
-- Name: T_TR_INVOICE_DET_INDT_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_TR_INVOICE_DET_INDT_ID_seq"', 42, true);


--
-- Name: T_TR_INVOICE_INVC_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_TR_INVOICE_INVC_ID_seq"', 18, true);


--
-- Name: T_TR_ITEM_RESERVATION_ITRS_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_TR_ITEM_RESERVATION_ITRS_ID_seq"', 29, true);


--
-- Name: T_TR_PAYMENT_PAYT_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_TR_PAYMENT_PAYT_ID_seq"', 20, true);


--
-- Name: T_TR_REPORT_HISTORY_RPHT_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_TR_REPORT_HISTORY_RPHT_ID_seq"', 108, true);


--
-- Name: T_TR_ROOM_RESERVATION_RORE_ID_seq; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE"."T_TR_ROOM_RESERVATION_RORE_ID_seq"', 42, true);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: LAKDERANA_BASE; Owner: postgres
--

SELECT pg_catalog.setval('"LAKDERANA_BASE".hibernate_sequence', 67, true);


--
-- Name: T_MS_ROOM "t_ms_room"_pk; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROOM"
    ADD CONSTRAINT """t_ms_room""_pk" PRIMARY KEY ("ROOM_ID");


--
-- Name: T_TR_INVOICE_DET "t_tr_invoice_det"_pk; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET"
    ADD CONSTRAINT """t_tr_invoice_det""_pk" PRIMARY KEY ("INDT_ID");


--
-- Name: T_PR_SYS_REF_NO_PARAM NMPR_PK; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_PR_SYS_REF_NO_PARAM"
    ADD CONSTRAINT "NMPR_PK" PRIMARY KEY ("NMPR_SEQ_NO");


--
-- Name: T_MS_ATTENDANCE PK_T_MS_ATTENDANCE; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ATTENDANCE"
    ADD CONSTRAINT "PK_T_MS_ATTENDANCE" PRIMARY KEY ("ATTN_ID");


--
-- Name: T_MS_DEPARTMENT PK_T_MS_DEPARTMENT; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_DEPARTMENT"
    ADD CONSTRAINT "PK_T_MS_DEPARTMENT" PRIMARY KEY ("DPMT_CODE");


--
-- Name: T_MS_FACILITY PK_T_MS_FACILITY; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_FACILITY"
    ADD CONSTRAINT "PK_T_MS_FACILITY" PRIMARY KEY ("FCLT_ID");


--
-- Name: T_MS_FUNCTION PK_T_MS_FUNCTION; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_FUNCTION"
    ADD CONSTRAINT "PK_T_MS_FUNCTION" PRIMARY KEY ("FUNC_ID");


--
-- Name: T_MS_INQUIRY PK_T_MS_INQUIRY; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_INQUIRY"
    ADD CONSTRAINT "PK_T_MS_INQUIRY" PRIMARY KEY ("INQR_ID");


--
-- Name: T_MS_ITEM PK_T_MS_ITEM; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ITEM"
    ADD CONSTRAINT "PK_T_MS_ITEM" PRIMARY KEY ("ITEM_ID");


--
-- Name: T_MS_LEAVE PK_T_MS_LEAVE; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_LEAVE"
    ADD CONSTRAINT "PK_T_MS_LEAVE" PRIMARY KEY ("LEAV_ID");


--
-- Name: T_MS_PARTY PK_T_MS_PARTY; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY"
    ADD CONSTRAINT "PK_T_MS_PARTY" PRIMARY KEY ("PRTY_CODE");


--
-- Name: T_MS_PARTY_CONTACT PK_T_MS_PARTY_CONTACT; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"
    ADD CONSTRAINT "PK_T_MS_PARTY_CONTACT" PRIMARY KEY ("PTCN_ID");


--
-- Name: T_MS_RESERVATION PK_T_MS_RESERVATION; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_RESERVATION"
    ADD CONSTRAINT "PK_T_MS_RESERVATION" PRIMARY KEY ("RESV_ID");


--
-- Name: T_MS_ROLE PK_T_MS_ROLE; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROLE"
    ADD CONSTRAINT "PK_T_MS_ROLE" PRIMARY KEY ("ROLE_ID");


--
-- Name: T_MS_ROLE_FUNCTION PK_T_MS_ROLE_FUNCTION; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION"
    ADD CONSTRAINT "PK_T_MS_ROLE_FUNCTION" PRIMARY KEY ("ROFU_ID");


--
-- Name: T_MS_SALARY PK_T_MS_SALARY; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_SALARY"
    ADD CONSTRAINT "PK_T_MS_SALARY" PRIMARY KEY ("SALR_ID");


--
-- Name: T_MS_USER PK_T_MS_USER; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER"
    ADD CONSTRAINT "PK_T_MS_USER" PRIMARY KEY ("USER_ID");


--
-- Name: T_MS_USER_BRANCH PK_T_MS_USER_BRANCH; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER_BRANCH"
    ADD CONSTRAINT "PK_T_MS_USER_BRANCH" PRIMARY KEY ("USBR_ID");


--
-- Name: T_RF_BRANCH PK_T_RF_BRANCH; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_RF_BRANCH"
    ADD CONSTRAINT "PK_T_RF_BRANCH" PRIMARY KEY ("BRNH_ID");


--
-- Name: T_RF_COMMON_REFERENCE PK_T_RF_COMMON_REFERENCE; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"
    ADD CONSTRAINT "PK_T_RF_COMMON_REFERENCE" PRIMARY KEY ("CMRF_CODE");


--
-- Name: T_RF_COMMON_REFERENCE_TYPE PK_T_RF_COMMON_REFERENCE_TYPE; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE_TYPE"
    ADD CONSTRAINT "PK_T_RF_COMMON_REFERENCE_TYPE" PRIMARY KEY ("CMRT_CODE");


--
-- Name: T_RF_REPORT_TYPE PK_T_RF_REPORT_TYPE; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_RF_REPORT_TYPE"
    ADD CONSTRAINT "PK_T_RF_REPORT_TYPE" PRIMARY KEY ("RPTP_ID");


--
-- Name: T_TR_FACILITY_RESERVATION PK_T_TR_FACILITY_RESERVATION; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION"
    ADD CONSTRAINT "PK_T_TR_FACILITY_RESERVATION" PRIMARY KEY ("FARE_ID");


--
-- Name: T_TR_INVOICE PK_T_TR_INVOICE; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE"
    ADD CONSTRAINT "PK_T_TR_INVOICE" PRIMARY KEY ("INVC_ID");


--
-- Name: T_TR_ITEM_RESERVATION PK_T_TR_ITEM_RESERVATION; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION"
    ADD CONSTRAINT "PK_T_TR_ITEM_RESERVATION" PRIMARY KEY ("ITRS_ID");


--
-- Name: T_TR_PAYMENT PK_T_TR_PAYMENT; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_PAYMENT"
    ADD CONSTRAINT "PK_T_TR_PAYMENT" PRIMARY KEY ("PAYT_ID");


--
-- Name: T_TR_REPORT_HISTORY PK_T_TR_REPORT_HISTORY; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_REPORT_HISTORY"
    ADD CONSTRAINT "PK_T_TR_REPORT_HISTORY" PRIMARY KEY ("RPHT_ID");


--
-- Name: T_TR_ROOM_RESERVATION PK_T_TR_ROOM_RESERVATION; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION"
    ADD CONSTRAINT "PK_T_TR_ROOM_RESERVATION" PRIMARY KEY ("RORE_ID");


--
-- Name: T_MS_PARTY_TOKEN TOKN_PK; Type: CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"
    ADD CONSTRAINT "TOKN_PK" PRIMARY KEY ("TOKN_SEQ_NO");


--
-- Name: T_RF_COMMON_REFERENCE_TYPE PK_T_RF_COMMON_REFERENCE_TYPE; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."T_RF_COMMON_REFERENCE_TYPE"
    ADD CONSTRAINT "PK_T_RF_COMMON_REFERENCE_TYPE" PRIMARY KEY ("CMRT_CODE");


--
-- Name: "t_ms_attendance"_"attn_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_attendance""_""attn_id""_uindex" ON "LAKDERANA_BASE"."T_MS_ATTENDANCE" USING btree ("ATTN_ID");


--
-- Name: "t_ms_department"_"dpmt_code"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_department""_""dpmt_code""_uindex" ON "LAKDERANA_BASE"."T_MS_DEPARTMENT" USING btree ("DPMT_CODE");


--
-- Name: "t_ms_facility"_"fclt_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_facility""_""fclt_id""_uindex" ON "LAKDERANA_BASE"."T_MS_FACILITY" USING btree ("FCLT_ID");


--
-- Name: "t_ms_inquiry"_"inqr_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_inquiry""_""inqr_id""_uindex" ON "LAKDERANA_BASE"."T_MS_INQUIRY" USING btree ("INQR_ID");


--
-- Name: "t_ms_item"_"item_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_item""_""item_id""_uindex" ON "LAKDERANA_BASE"."T_MS_ITEM" USING btree ("ITEM_ID");


--
-- Name: "t_ms_leave"_"leav_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_leave""_""leav_id""_uindex" ON "LAKDERANA_BASE"."T_MS_LEAVE" USING btree ("LEAV_ID");


--
-- Name: "t_ms_party_contact"_"ptcn_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_party_contact""_""ptcn_id""_uindex" ON "LAKDERANA_BASE"."T_MS_PARTY_CONTACT" USING btree ("PTCN_ID");


--
-- Name: "t_ms_reservation"_"resv_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_reservation""_""resv_id""_uindex" ON "LAKDERANA_BASE"."T_MS_RESERVATION" USING btree ("RESV_ID");


--
-- Name: "t_ms_room"_"room_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_room""_""room_id""_uindex" ON "LAKDERANA_BASE"."T_MS_ROOM" USING btree ("ROOM_ID");


--
-- Name: "t_ms_salary"_"salr_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_salary""_""salr_id""_uindex" ON "LAKDERANA_BASE"."T_MS_SALARY" USING btree ("SALR_ID");


--
-- Name: "t_ms_user_branch"_"usbr_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_ms_user_branch""_""usbr_id""_uindex" ON "LAKDERANA_BASE"."T_MS_USER_BRANCH" USING btree ("USBR_ID");


--
-- Name: "t_rf_branch"_"brnh_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_rf_branch""_""brnh_id""_uindex" ON "LAKDERANA_BASE"."T_RF_BRANCH" USING btree ("BRNH_ID");


--
-- Name: "t_rf_common_reference_type"_"cmrt_code"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_rf_common_reference_type""_""cmrt_code""_uindex" ON "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE_TYPE" USING btree ("CMRT_CODE");


--
-- Name: "t_rf_report_type"_"rptp_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_rf_report_type""_""rptp_id""_uindex" ON "LAKDERANA_BASE"."T_RF_REPORT_TYPE" USING btree ("RPTP_ID");


--
-- Name: "t_tr_invoice"_"invc_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_tr_invoice""_""invc_id""_uindex" ON "LAKDERANA_BASE"."T_TR_INVOICE" USING btree ("INVC_ID");


--
-- Name: "t_tr_invoice_det"_"indt_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_tr_invoice_det""_""indt_id""_uindex" ON "LAKDERANA_BASE"."T_TR_INVOICE_DET" USING btree ("INDT_ID");


--
-- Name: "t_tr_item_reservation"_"itrs_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_tr_item_reservation""_""itrs_id""_uindex" ON "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION" USING btree ("ITRS_ID");


--
-- Name: "t_tr_payment"_"payt_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_tr_payment""_""payt_id""_uindex" ON "LAKDERANA_BASE"."T_TR_PAYMENT" USING btree ("PAYT_ID");


--
-- Name: "t_tr_report_history"_"rpht_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_tr_report_history""_""rpht_id""_uindex" ON "LAKDERANA_BASE"."T_TR_REPORT_HISTORY" USING btree ("RPHT_ID");


--
-- Name: "t_tr_room_reservation"_"rore_id"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX """t_tr_room_reservation""_""rore_id""_uindex" ON "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION" USING btree ("RORE_ID");


--
-- Name: I_TOKN_PRTY_FK; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE INDEX "I_TOKN_PRTY_FK" ON "LAKDERANA_BASE"."T_MS_PARTY_TOKEN" USING btree ("TOKN_PARTY_CODE");


--
-- Name: t_ms_party_"prty_code"_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX "t_ms_party_""prty_code""_uindex" ON "LAKDERANA_BASE"."T_MS_PARTY" USING btree ("PRTY_CODE");


--
-- Name: t_ms_party_prty_nic_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX t_ms_party_prty_nic_uindex ON "LAKDERANA_BASE"."T_MS_PARTY" USING btree ("PRTY_NIC");


--
-- Name: t_ms_party_prty_passport_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX t_ms_party_prty_passport_uindex ON "LAKDERANA_BASE"."T_MS_PARTY" USING btree ("PRTY_PASSPORT");


--
-- Name: t_ms_role_role_name_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX t_ms_role_role_name_uindex ON "LAKDERANA_BASE"."T_MS_ROLE" USING btree ("ROLE_NAME");


--
-- Name: t_ms_user_user_username_uindex; Type: INDEX; Schema: LAKDERANA_BASE; Owner: postgres
--

CREATE UNIQUE INDEX t_ms_user_user_username_uindex ON "LAKDERANA_BASE"."T_MS_USER" USING btree ("USER_USERNAME");


--
-- Name: "t_rf_common_reference_type"_"cmrt_code"_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX """t_rf_common_reference_type""_""cmrt_code""_uindex" ON public."T_RF_COMMON_REFERENCE_TYPE" USING btree ("CMRT_CODE");


--
-- Name: T_MS_ATTENDANCE FK_ATTN_EMPLOYEE_CODE_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ATTENDANCE"
    ADD CONSTRAINT "FK_ATTN_EMPLOYEE_CODE_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("ATTN_EMPLOYEE_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- Name: T_RF_COMMON_REFERENCE FK_CMRF_CMRT_CODE_T_RF_COMMON_REFERENCE_TYPE_CMRT_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"
    ADD CONSTRAINT "FK_CMRF_CMRT_CODE_T_RF_COMMON_REFERENCE_TYPE_CMRT_CODE" FOREIGN KEY ("CMRF_CMRT_CODE") REFERENCES "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE_TYPE"("CMRT_CODE");


--
-- Name: T_MS_FACILITY FK_FALT_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_FACILITY"
    ADD CONSTRAINT "FK_FALT_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("FCLT_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_TR_FACILITY_RESERVATION FK_FARE_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION"
    ADD CONSTRAINT "FK_FARE_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("FARE_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_TR_FACILITY_RESERVATION FK_FARE_FACILITY_ID_T_MS_FACILITY_FCLT_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION"
    ADD CONSTRAINT "FK_FARE_FACILITY_ID_T_MS_FACILITY_FCLT_ID" FOREIGN KEY ("FARE_FACILITY_ID") REFERENCES "LAKDERANA_BASE"."T_MS_FACILITY"("FCLT_ID");


--
-- Name: T_TR_FACILITY_RESERVATION FK_FARE_RESERVATION_ID_T_MS_RESERVATION_RESV_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION"
    ADD CONSTRAINT "FK_FARE_RESERVATION_ID_T_MS_RESERVATION_RESV_ID" FOREIGN KEY ("FARE_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_MS_RESERVATION"("RESV_ID");


--
-- Name: T_MS_FACILITY FK_FCLT_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_FACILITY"
    ADD CONSTRAINT "FK_FCLT_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE" FOREIGN KEY ("FCLT_TYPE") REFERENCES "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"("CMRF_CODE");


--
-- Name: T_TR_INVOICE_DET FK_INDT_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET"
    ADD CONSTRAINT "FK_INDT_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("INDT_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_MS_ATTENDANCE FK_INDT_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ATTENDANCE"
    ADD CONSTRAINT "FK_INDT_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("ATTN_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_TR_INVOICE_DET FK_INDT_INVOICE_ID_T_TR_INVOICE_INVC_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET"
    ADD CONSTRAINT "FK_INDT_INVOICE_ID_T_TR_INVOICE_INVC_ID" FOREIGN KEY ("INDT_INVOICE_ID") REFERENCES "LAKDERANA_BASE"."T_TR_INVOICE"("INVC_ID");


--
-- Name: T_TR_INVOICE_DET FK_INDT_RESERVATION_ID_T_MS_RESERVATION_RESV_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET"
    ADD CONSTRAINT "FK_INDT_RESERVATION_ID_T_MS_RESERVATION_RESV_ID" FOREIGN KEY ("INDT_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_MS_RESERVATION"("RESV_ID");


--
-- Name: T_TR_INVOICE_DET FK_INDT_ROOM_RESERVATION_ID_T_TR_ROOM_RESERVATION_RORE_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET"
    ADD CONSTRAINT "FK_INDT_ROOM_RESERVATION_ID_T_TR_ROOM_RESERVATION_RORE_ID" FOREIGN KEY ("INDT_ROOM_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION"("RORE_ID");


--
-- Name: T_MS_INQUIRY FK_INQR_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_INQUIRY"
    ADD CONSTRAINT "FK_INQR_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("INQR_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_MS_INQUIRY FK_INQR_CUSTOMER_CODE_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_INQUIRY"
    ADD CONSTRAINT "FK_INQR_CUSTOMER_CODE_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("INQR_CUSTOMER_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- Name: T_MS_INQUIRY FK_INQR_TRANSFERRED_FROM_T_RF_BRANCH; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_INQUIRY"
    ADD CONSTRAINT "FK_INQR_TRANSFERRED_FROM_T_RF_BRANCH" FOREIGN KEY ("INQR_TRANSFERRED_FROM") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_TR_INVOICE FK_INVC_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE"
    ADD CONSTRAINT "FK_INVC_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("INVC_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_MS_ITEM FK_ITEM_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ITEM"
    ADD CONSTRAINT "FK_ITEM_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("ITEM_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_REPORT_TYPE"("RPTP_ID");


--
-- Name: T_TR_ITEM_RESERVATION FK_ITRS_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION"
    ADD CONSTRAINT "FK_ITRS_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("ITRS_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_REPORT_TYPE"("RPTP_ID");


--
-- Name: T_TR_ITEM_RESERVATION FK_ITRS_ITEM_ID_T_MS_ITEM_ITEM_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION"
    ADD CONSTRAINT "FK_ITRS_ITEM_ID_T_MS_ITEM_ITEM_ID" FOREIGN KEY ("ITRS_ITEM_ID") REFERENCES "LAKDERANA_BASE"."T_MS_ITEM"("ITEM_ID");


--
-- Name: T_MS_LEAVE FK_LEAV_EMPLOYEE_CODE_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_LEAVE"
    ADD CONSTRAINT "FK_LEAV_EMPLOYEE_CODE_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("LEAV_EMPLOYEE_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- Name: T_MS_LEAVE FK_LEAV_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_LEAVE"
    ADD CONSTRAINT "FK_LEAV_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE" FOREIGN KEY ("LEAV_TYPE") REFERENCES "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"("CMRF_CODE");


--
-- Name: T_TR_PAYMENT FK_PAYT_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_PAYMENT"
    ADD CONSTRAINT "FK_PAYT_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("PAYT_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_TR_PAYMENT FK_PAYT_RESERVATION_ID_T_MS_RESERVATION_RESV_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_PAYMENT"
    ADD CONSTRAINT "FK_PAYT_RESERVATION_ID_T_MS_RESERVATION_RESV_ID" FOREIGN KEY ("PAYT_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_MS_RESERVATION"("RESV_ID");


--
-- Name: T_TR_PAYMENT FK_PAYT_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_PAYMENT"
    ADD CONSTRAINT "FK_PAYT_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE" FOREIGN KEY ("PAYT_TYPE") REFERENCES "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"("CMRF_CODE");


--
-- Name: T_MS_PARTY FK_PRTY_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY"
    ADD CONSTRAINT "FK_PRTY_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("PRTY_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_MS_PARTY FK_PRTY_DEPARTMENT_CODE_T_MS_DEPARTMENT_DPMT_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY"
    ADD CONSTRAINT "FK_PRTY_DEPARTMENT_CODE_T_MS_DEPARTMENT_DPMT_CODE" FOREIGN KEY ("PRTY_DEPARTMENT_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_DEPARTMENT"("DPMT_CODE");


--
-- Name: T_MS_PARTY FK_PRTY_MANAGED_BY_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY"
    ADD CONSTRAINT "FK_PRTY_MANAGED_BY_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("PRTY_MANAGED_BY") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- Name: T_MS_PARTY FK_PRTY_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY"
    ADD CONSTRAINT "FK_PRTY_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE" FOREIGN KEY ("PRTY_TYPE") REFERENCES "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"("CMRF_CODE");


--
-- Name: T_MS_PARTY_CONTACT FK_PTCN_CONTACT_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"
    ADD CONSTRAINT "FK_PTCN_CONTACT_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE" FOREIGN KEY ("PTCN_CONTACT_TYPE") REFERENCES "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"("CMRF_CODE");


--
-- Name: T_MS_PARTY_CONTACT FK_PTCN_PRTY_CODE_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY_CONTACT"
    ADD CONSTRAINT "FK_PTCN_PRTY_CODE_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("PTCN_PRTY_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- Name: T_MS_RESERVATION FK_RESV_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_RESERVATION"
    ADD CONSTRAINT "FK_RESV_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("RESV_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_MS_RESERVATION FK_RESV_INQUIRY_ID_T_MS_INQUIRY_INQR_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_RESERVATION"
    ADD CONSTRAINT "FK_RESV_INQUIRY_ID_T_MS_INQUIRY_INQR_ID" FOREIGN KEY ("RESV_INQUIRY_ID") REFERENCES "LAKDERANA_BASE"."T_MS_INQUIRY"("INQR_ID");


--
-- Name: T_MS_ROOM FK_ROOM_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROOM"
    ADD CONSTRAINT "FK_ROOM_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("ROOM_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_MS_ROOM FK_ROOM_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROOM"
    ADD CONSTRAINT "FK_ROOM_TYPE_T_RF_COMMON_REFERENCE_CMRF_CODE" FOREIGN KEY ("ROOM_TYPE") REFERENCES "LAKDERANA_BASE"."T_RF_COMMON_REFERENCE"("CMRF_CODE");


--
-- Name: T_TR_ROOM_RESERVATION FK_RORE_BRANCH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION"
    ADD CONSTRAINT "FK_RORE_BRANCH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("RORE_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_TR_ROOM_RESERVATION FK_RORE_RESERVATION_ID_T_MS_RESERVATION_RESV_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION"
    ADD CONSTRAINT "FK_RORE_RESERVATION_ID_T_MS_RESERVATION_RESV_ID" FOREIGN KEY ("RORE_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_MS_RESERVATION"("RESV_ID");


--
-- Name: T_TR_ROOM_RESERVATION FK_RORE_ROOM_ID_T_MS_ROOM_ROOM_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ROOM_RESERVATION"
    ADD CONSTRAINT "FK_RORE_ROOM_ID_T_MS_ROOM_ROOM_ID" FOREIGN KEY ("RORE_ROOM_ID") REFERENCES "LAKDERANA_BASE"."T_MS_ROOM"("ROOM_ID");


--
-- Name: T_TR_REPORT_HISTORY FK_RPHT_BRANH_ID_T_RF_BRANCH_BRNH_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_REPORT_HISTORY"
    ADD CONSTRAINT "FK_RPHT_BRANH_ID_T_RF_BRANCH_BRNH_ID" FOREIGN KEY ("RPHT_BRANH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_TR_REPORT_HISTORY FK_RPHT_REPORT_TYPE_T_RF_REPORT_TYPE_RPTP_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_REPORT_HISTORY"
    ADD CONSTRAINT "FK_RPHT_REPORT_TYPE_T_RF_REPORT_TYPE_RPTP_ID" FOREIGN KEY ("RPHT_REPORT_TYPE") REFERENCES "LAKDERANA_BASE"."T_RF_REPORT_TYPE"("RPTP_ID");


--
-- Name: T_MS_SALARY FK_SALR_EMPLOYEE_CODE_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_SALARY"
    ADD CONSTRAINT "FK_SALR_EMPLOYEE_CODE_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("SALR_EMPLOYEE_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- Name: T_TR_ITEM_RESERVATION FK_T_MS_RESERVATION_RESV_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION"
    ADD CONSTRAINT "FK_T_MS_RESERVATION_RESV_ID" FOREIGN KEY ("ITRS_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_MS_RESERVATION"("RESV_ID");


--
-- Name: T_MS_ROLE_FUNCTION FK_T_MS_ROLE_FUNCTION_ROFU_FUNC_ID_T_MS_FUNCTION_FUNC_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION"
    ADD CONSTRAINT "FK_T_MS_ROLE_FUNCTION_ROFU_FUNC_ID_T_MS_FUNCTION_FUNC_ID" FOREIGN KEY ("ROFU_FUNC_ID") REFERENCES "LAKDERANA_BASE"."T_MS_FUNCTION"("FUNC_ID");


--
-- Name: T_MS_ROLE_FUNCTION FK_T_MS_ROLE_FUNCTION_ROFU_ROLE_ID_T_MS_ROLE_ROLE_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_ROLE_FUNCTION"
    ADD CONSTRAINT "FK_T_MS_ROLE_FUNCTION_ROFU_ROLE_ID_T_MS_ROLE_ROLE_ID" FOREIGN KEY ("ROFU_ROLE_ID") REFERENCES "LAKDERANA_BASE"."T_MS_ROLE"("ROLE_ID");


--
-- Name: T_MS_USER_ROLE FK_T_RF_USER_ROLES_USRL_ROLE_ID_T_MS_ROLE_ROLE_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER_ROLE"
    ADD CONSTRAINT "FK_T_RF_USER_ROLES_USRL_ROLE_ID_T_MS_ROLE_ROLE_ID" FOREIGN KEY ("USRL_ROLE_ID") REFERENCES "LAKDERANA_BASE"."T_MS_ROLE"("ROLE_ID");


--
-- Name: T_MS_USER_ROLE FK_T_RF_USER_ROLES_USRL_USER_ID_T_MS_USER_USER_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER_ROLE"
    ADD CONSTRAINT "FK_T_RF_USER_ROLES_USRL_USER_ID_T_MS_USER_USER_ID" FOREIGN KEY ("USRL_USER_ID") REFERENCES "LAKDERANA_BASE"."T_MS_USER"("USER_ID");


--
-- Name: T_TR_INVOICE_DET FK_T_TR_FACILITY_RESERVATION_FARE_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET"
    ADD CONSTRAINT "FK_T_TR_FACILITY_RESERVATION_FARE_ID" FOREIGN KEY ("INDT_FACILITY_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_TR_FACILITY_RESERVATION"("FARE_ID");


--
-- Name: T_TR_INVOICE_DET FK_T_TR_ITEM_RESERVATION_ITRS_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE_DET"
    ADD CONSTRAINT "FK_T_TR_ITEM_RESERVATION_ITRS_ID" FOREIGN KEY ("INDT_ITEM_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_TR_ITEM_RESERVATION"("ITRS_ID");


--
-- Name: T_TR_INVOICE FK_T_TR_ITEM_RESERVATION_ITRS_ID; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_TR_INVOICE"
    ADD CONSTRAINT "FK_T_TR_ITEM_RESERVATION_ITRS_ID" FOREIGN KEY ("INVC_RESERVATION_ID") REFERENCES "LAKDERANA_BASE"."T_MS_RESERVATION"("RESV_ID");


--
-- Name: T_MS_USER_BRANCH FK_USBR_BRANCH_ID_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER_BRANCH"
    ADD CONSTRAINT "FK_USBR_BRANCH_ID_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("USBR_BRANCH_ID") REFERENCES "LAKDERANA_BASE"."T_RF_BRANCH"("BRNH_ID");


--
-- Name: T_MS_USER_BRANCH FK_USBR_USER_ID_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER_BRANCH"
    ADD CONSTRAINT "FK_USBR_USER_ID_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("USBR_USER_ID") REFERENCES "LAKDERANA_BASE"."T_MS_USER"("USER_ID");


--
-- Name: T_MS_USER FK_USER_PARTY_CODE_T_MS_PARTY_PRTY_CODE; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_USER"
    ADD CONSTRAINT "FK_USER_PARTY_CODE_T_MS_PARTY_PRTY_CODE" FOREIGN KEY ("USER_PARTY_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- Name: T_MS_PARTY_TOKEN TOKN_PRTY_FK; Type: FK CONSTRAINT; Schema: LAKDERANA_BASE; Owner: postgres
--

ALTER TABLE ONLY "LAKDERANA_BASE"."T_MS_PARTY_TOKEN"
    ADD CONSTRAINT "TOKN_PRTY_FK" FOREIGN KEY ("TOKN_PARTY_CODE") REFERENCES "LAKDERANA_BASE"."T_MS_PARTY"("PRTY_CODE");


--
-- PostgreSQL database dump complete
--

