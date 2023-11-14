--
-- UTL_FTP  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.UTL_FTP
AS

   /**
    * LICENSE: GNU Lesser General Public License (LGPL)
    * Copyright (C) 2003-2006  Russ Johnson (john_2885@yahoo.com)
    *
    * This library is free software; you can redistribute it and/or
    * modify it under the terms of the GNU Lesser General Public
    * License, version 2.1, as published by the Free Software Foundation.
    *
    * This library is distributed in the hope that it will be useful,
    * but WITHOUT ANY WARRANTY; without even the implied warranty of
    * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    * Lesser General Public License for more details.
    *
    * You should have received a copy of the GNU Lesser General Public
    * License along with this library; if not, write to the Free Software
    * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
    *
    * For full license details please visit www.gnu.org
    */

    TSFR_START_CODE1  CONSTANT PLS_INTEGER  := 125;
    TSFR_START_CODE2  CONSTANT PLS_INTEGER  := 150;
    TYPE_CODE         CONSTANT PLS_INTEGER  := 200;
    SYST_CODE         CONSTANT PLS_INTEGER  := 215;
    CONNECT_CODE      CONSTANT PLS_INTEGER  := 220;
    QUIT_CODE         CONSTANT PLS_INTEGER  := 221;
    TSFR_END_CODE     CONSTANT PLS_INTEGER  := 226;
    PASV_CODE         CONSTANT PLS_INTEGER  := 227;
    LOGIN_CODE        CONSTANT PLS_INTEGER  := 230;
    CWD_CODE          CONSTANT PLS_INTEGER  := 250;
    PWD_CODE          CONSTANT PLS_INTEGER  := 257;
    USER_CODE         CONSTANT PLS_INTEGER  := 331;

    FTP_SUCCESS       CONSTANT VARCHAR2(10) := 'SUCCESS';
    FTP_ERROR         CONSTANT VARCHAR2(10) := 'ERROR';
    FTP_SUCCESS_MSG   CONSTANT VARCHAR2(40) := 'FTP SUCCESSFUL';
    FTP_ERROR_MSG     CONSTANT VARCHAR2(40) := 'FTP FAILURE';


    ------------------------------------------------------------------------------------
    --  Reads a single or multi-line reply from the FTP server
    --
    --  Return TRUE if reply code matches p_code1 or p_code2,
    --  FALSE if it doesn't or error occurs
    --
    --  Send full server response back to calling procedure
    ------------------------------------------------------------------------------------

    PROCEDURE VALIDATE_REPLY(p_ctrl_con  IN OUT UTL_TCP.CONNECTION,
                             p_code1     IN     PLS_INTEGER,
                             p_code2     IN     PLS_INTEGER DEFAULT NULL,
                             p_reply     OUT    VARCHAR2)
    IS
        n_code1      PLS_INTEGER    := p_code1;
        n_code2      PLS_INTEGER    := p_code2;
        v_msg        VARCHAR2(255);
        v_reply      VARCHAR2(255)  := '';
        n_line_count PLS_INTEGER    := 0;
        b_multi      BOOLEAN        := FALSE;
        v_multi_code VARCHAR2(3);

    BEGIN

        -- DBMS_OUTPUT.PUT_LINE('Expected Code 1: '||TO_CHAR(n_code1));
        -- DBMS_OUTPUT.PUT_LINE('Expected Code 2: '||NVL(TO_CHAR(n_code2),'NULL'));

        LOOP
            v_msg := SUBSTR(UTL_TCP.GET_LINE(p_ctrl_con),1,255);
            v_reply := SUBSTR(v_reply||v_msg,1,255);
            n_line_count := n_line_count + 1;

            -- DBMS_OUTPUT.PUT_LINE(v_msg);

            IF n_line_count = 1
            THEN
                IF SUBSTR(v_msg,4,1) = '-'
                THEN
                    b_multi      := TRUE;
                    v_multi_code := SUBSTR(v_msg,1,3);
                    -- DBMS_OUTPUT.PUT_LINE('Multicode: '||v_multi_code);

                END IF;

            ELSIF n_line_count > 1
            THEN
                IF SUBSTR(v_msg,4,1) != '-' AND SUBSTR(v_msg,1,3) = v_multi_code
                THEN
                    b_multi := FALSE;

                END IF;

            END IF;

            p_reply := v_reply;

            IF b_multi = FALSE
            THEN
                EXIT;

            END IF;

        END LOOP;

        -- DBMS_OUTPUT.PUT_LINE(p_reply);

        IF TO_NUMBER(SUBSTR(p_reply,1,3)) NOT IN(n_code1,NVL(n_code2,n_code1))
        THEN
            RAISE_APPLICATION_ERROR(FTP_EXCEPTION_ERRCODE,v_reply);

        END IF;

    EXCEPTION
    WHEN OTHERS
    THEN
        RAISE_APPLICATION_ERROR(FTP_EXCEPTION_ERRCODE,SUBSTR(SQLERRM,1,255));

    END VALIDATE_REPLY;

    ------------------------------------------------------------------
    -- Log in to the FTP server
    -- If successful, return a UTL_TCP.CONNECTION
    ------------------------------------------------------------------

    FUNCTION LOGIN(p_hostname IN VARCHAR2,
                   p_username IN VARCHAR2,
                   p_password IN VARCHAR2,
                   p_port     IN PLS_INTEGER)
    RETURN UTL_TCP.CONNECTION
    IS
        n_byte_count    PLS_INTEGER;
        u_ctrl_con      UTL_TCP.CONNECTION;
        v_reply         VARCHAR2(255);


    BEGIN

        --------------------------------------------------
        -- Attempt to connect to the host machine
        --------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Opening Connection');
        u_ctrl_con := UTL_TCP.OPEN_CONNECTION(p_hostname,p_port);
        VALIDATE_REPLY(u_ctrl_con,CONNECT_CODE,NULL,v_reply);

        --------------------------------------------------
        -- Send username
        --------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Sending Username');
        n_byte_count := UTL_TCP.WRITE_LINE(u_ctrl_con,'USER '||p_username);

        VALIDATE_REPLY(u_ctrl_con,USER_CODE,NULL,v_reply);

        --------------------------------------------------
        -- Send password
        --------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Sending Password');
        n_byte_count := UTL_TCP.WRITE_LINE(u_ctrl_con,'PASS '||p_password);
        VALIDATE_REPLY(u_ctrl_con,LOGIN_CODE,NULL,v_reply);

        RETURN u_ctrl_con;

    EXCEPTION
    WHEN OTHERS
    THEN
        RAISE;

    END;

    ------------------------------------------------------------------
    -- This procedure changes the remote working directory via
    -- the CWD command
    ------------------------------------------------------------------

    PROCEDURE CWD(p_ctrl_con   IN OUT UTL_TCP.CONNECTION,
                  p_remotepath IN     VARCHAR2)
    IS
        n_byte_count    PLS_INTEGER;
        v_cmd           VARCHAR2(20) := 'CWD ';
        v_reply         VARCHAR2(255);

    BEGIN
        --------------------------------------------------
        -- Change the remote directory
        --------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Changing Remote Working Directory');
        n_byte_count := UTL_TCP.WRITE_LINE(p_ctrl_con,v_cmd||p_remotepath);
        VALIDATE_REPLY(p_ctrl_con,CWD_CODE,NULL,v_reply);

    EXCEPTION
    WHEN OTHERS
    THEN
        RAISE;

    END;

    ------------------------------------------------------------------
    -- This procedure quits the control connection
    ------------------------------------------------------------------

    PROCEDURE QUIT(p_ctrl_con   IN OUT UTL_TCP.CONNECTION)
    IS
        n_byte_count    PLS_INTEGER;
        v_cmd           VARCHAR2(20) := 'QUIT';
        v_reply         VARCHAR2(255);

    BEGIN

        ------------------------------------
        -- Send QUIT command
        ------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Quitting Connection');
        n_byte_count := UTL_TCP.WRITE_LINE(p_ctrl_con,v_cmd);

        -------------------------------------------------------------
        -- Don't need to validate QUIT, just close the connection
        -------------------------------------------------------------
        UTL_TCP.CLOSE_CONNECTION(p_ctrl_con);

    EXCEPTION
    WHEN OTHERS
    THEN
        RAISE;

    END;

    ------------------------------------------------------------------
    -- This procedure sets the transfer type (ASCII or BINARY)
    -- A = ASCII
    -- I = IMAGE (BINARY)
    ------------------------------------------------------------------

    PROCEDURE SET_TRANSFER_MODE(p_ctrl_con  IN OUT UTL_TCP.CONNECTION,
                                p_mode      IN VARCHAR2)
    IS

        n_byte_count    PLS_INTEGER;
        v_reply         VARCHAR2(255);
        v_ascii         VARCHAR2(10) := 'TYPE A';
        v_binary        VARCHAR2(10) := 'TYPE I';

    BEGIN

        -- DBMS_OUTPUT.PUT_LINE('Setting Transfer Type');

        IF UPPER(p_mode) = 'ASCII'
        THEN
            n_byte_count := UTL_TCP.WRITE_LINE(p_ctrl_con,v_ascii);

        ELSE
            n_byte_count := UTL_TCP.WRITE_LINE(p_ctrl_con,v_binary);

        END IF;
        VALIDATE_REPLY(p_ctrl_con,TYPE_CODE,NULL,v_reply);

    EXCEPTION
    WHEN OTHERS
    THEN
        RAISE;

    END;

    ------------------------------------------------------------------
    -- Create the passive host IP and port number to connect to
    ------------------------------------------------------------------

    PROCEDURE CREATE_PASSIVE(p_ctrl_con  IN OUT UTL_TCP.CONNECTION,
                             p_pasv_host OUT    VARCHAR2,
                             p_pasv_port OUT    NUMBER)
    IS

        -- Host and port to connect to for data transfer
        n_byte_count    PLS_INTEGER;
        v_pasv_cmd      VARCHAR2(10) := 'PASV';
        v_port_info     VARCHAR2(30);
        v_reply         VARCHAR2(255);
        n_port_dec      NUMBER;
        n_port_add      NUMBER;


    BEGIN

        -- DBMS_OUTPUT.PUT_LINE('50 - Creating Passive Connection');
        n_byte_count := UTL_TCP.WRITE_LINE(p_ctrl_con,v_pasv_cmd);
        VALIDATE_REPLY(p_ctrl_con,PASV_CODE,NULL,v_reply);
        -- DBMS_OUTPUT.PUT_LINE('Passive Response: ');
        --DBMS_OUTPUT.PUT_LINE(v_reply);

        -----------------------------------------------------------------
        --  2005.02.12 (raj) Added Comments
        --  Once a PASV command is sent to the server to request a passive
        --  connection, the server sends back the host and port connection
        --  info in the format (127,0,0,1,8,219) where the first four
        --  numbers are the octets of the IP Address, and the fifth and sixth
        --  numbers represent the port to connect to.  I could not find in RFC
        --  959 why the communication is like this, but to get the port number
        --  multiply the fifth number by 256 and add the sixth number.
        --  I am sure there is some network-type convention (possibly obsolete)
        --  that caused this, but I don't know what it is or why.  All I know
        --  is that it works
        -----------------------------------------------------------------

        -------------------------------------------------------------
        -- Remove the parentheses from the connection info string
        -------------------------------------------------------------
        v_port_info := SUBSTR(v_reply,INSTR(v_reply,'(',1,1)+1,INSTR(v_reply,')',1,1)-INSTR(v_reply,'(',1,1)-1);

        -------------------------------------------------------------
        -- Replace commas with dots to get the IP address
        -------------------------------------------------------------
        p_pasv_host := REPLACE(SUBSTR(v_port_info,1,INSTR(v_port_info,',',1,4)-1),',','.');

        -------------------------------------------------------------
        -- Extract the port info to calculate the port number
        -------------------------------------------------------------
        n_port_dec  := TO_NUMBER(SUBSTR(v_port_info,INSTR(v_port_info,',',1,4)+1,(INSTR(v_port_info,',',1,5)-(INSTR(v_port_info,',',1,4)+1))));
        n_port_add  := TO_NUMBER(SUBSTR(v_port_info,INSTR(v_port_info,',',1,5)+1,LENGTH(v_port_info)-INSTR(v_port_info,',',1,5)));

        -------------------------------------------------------------
        -- Calculate the port number to return as an OUT parameter
        -------------------------------------------------------------
        p_pasv_port := (n_port_dec*256) + n_port_add;


    EXCEPTION
    WHEN OTHERS
    THEN
        RAISE;

    END CREATE_PASSIVE;

    PROCEDURE TRANSFER_FILE(p_ctrl_con          IN OUT UTL_TCP.CONNECTION,
                            p_localpath         IN     VARCHAR2,
                            p_localname         IN     VARCHAR2,
                            p_remotename        IN     VARCHAR2 DEFAULT NULL,
                            p_pasv_host         IN     VARCHAR2,
                            p_pasv_port         IN     PLS_INTEGER,
                            p_transfer_mode     IN     VARCHAR2,
                            p_filetype          IN     VARCHAR2,
                            n_bytes_transmitted OUT    NUMBER,
                            d_trans_start       OUT    DATE,
                            d_trans_end         OUT    DATE)

    IS

        u_data_con      UTL_TCP.CONNECTION;
        u_filehandle    UTL_FILE.FILE_TYPE;
        v_tsfr_mode     VARCHAR2(3)      := p_transfer_mode;
        v_filetype      VARCHAR2(10)     := UPPER(p_filetype);
        v_tsfr_cmd      VARCHAR2(10);
        v_buffer        VARCHAR2(32767);
        v_localpath     VARCHAR2(255)   := p_localpath;
        v_localname     VARCHAR2(255)   := p_localname;
        v_remotename    VARCHAR2(255)   := p_remotename;
        v_host          VARCHAR2(20)    := p_pasv_host;
        n_port          PLS_INTEGER     := p_pasv_port;
        v_filename      VARCHAR2(255);
        n_bytes         NUMBER;
        v_msg           VARCHAR2(255);
        v_reply         VARCHAR2(1000);
        v_mode          VARCHAR2(1);

    BEGIN

        -- Initialize some of our OUT variables
        n_bytes_transmitted := 0;

        IF UPPER(v_tsfr_mode) = 'PUT'
        THEN
            v_mode     := 'r';
            v_tsfr_cmd := 'STOR ';

        ELSIF UPPER(v_tsfr_mode) = 'GET'
        THEN
            v_mode     := 'w';
            v_tsfr_cmd := 'RETR ';

        END IF;

        ---------------------------------------------------
        -- Open data connection on Passive host and port
        ---------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Opening the Data Connection...');
        u_data_con := UTL_TCP.OPEN_CONNECTION(v_host,n_port);

        ---------------------------------------------------
        -- Open the local file to read and transfer data
        ---------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Opening File...');
        -- DBMS_OUTPUT.PUT_LINE('Path: '||v_localpath);
        -- DBMS_OUTPUT.PUT_LINE('Name: '||v_localname);
        -- DBMS_OUTPUT.PUT_LINE('Mode: '||v_mode);
        u_filehandle := UTL_FILE.FOPEN(v_localpath,v_localname,v_mode,32767);

        --------------------------------------------------------------------------
        -- Send the STOR or RETR command to send or receive the file
        --------------------------------------------------------------------------
        IF v_remotename IS NOT NULL
        THEN
            v_filename := v_remotename;

        ELSE
            v_filename := v_localname;

        END IF;

        -- DBMS_OUTPUT.PUT_LINE('Sending Transfer Command...');
        n_bytes := UTL_TCP.WRITE_LINE(p_ctrl_con,v_tsfr_cmd||v_filename);
        VALIDATE_REPLY(p_ctrl_con,TSFR_START_CODE1,TSFR_START_CODE2,v_reply);


        d_trans_start := SYSDATE;

        IF UPPER(v_tsfr_mode) = 'PUT'
        THEN
            LOOP
                BEGIN
                   UTL_FILE.GET_LINE(u_filehandle,v_buffer);
                EXCEPTION
                WHEN NO_DATA_FOUND
                THEN
                    EXIT;
                END;

                n_bytes := UTL_TCP.WRITE_LINE(u_data_con,v_buffer);
                n_bytes_transmitted := n_bytes_transmitted + n_bytes;

            END LOOP;

        ELSIF UPPER(v_tsfr_mode) = 'GET'
        THEN
            LOOP
                BEGIN
                    IF v_filetype = 'ASCII'
                    THEN
                        v_buffer := UTL_TCP.GET_LINE(u_data_con,TRUE);

                    ELSE
                        v_buffer := UTL_TCP.GET_TEXT(u_data_con,32767);

                    END IF;

                    -----------------------------------------------------
                    -- Sometimes the TCP/IP buffer sends null data
                    -- we only want to receive the actual data
                    -----------------------------------------------------

                    IF v_buffer IS NOT NULL
                    THEN
                        IF v_filetype = 'ASCII'
                        THEN
                            UTL_FILE.PUT_LINE(u_filehandle,v_buffer);

                        ELSE
                            UTL_FILE.PUT(u_filehandle,v_buffer);

                        END IF;

                        n_bytes := LENGTH(v_buffer);
                        n_bytes_transmitted := n_bytes_transmitted + n_bytes;

                    END IF;

                EXCEPTION
                WHEN UTL_TCP.END_OF_INPUT
                THEN
                    EXIT;
                END;

            END LOOP;

        END IF;

        -- Flush the buffer on the data connection
        -- UTL_TCP.FLUSH(u_data_con);

        d_trans_end := SYSDATE;

        ---------------------------
        -- Close the file
        ---------------------------
        UTL_FILE.FCLOSE(u_filehandle);

        --------------------------------
        -- Close the Data Connection
        --------------------------------
        UTL_TCP.CLOSE_CONNECTION(u_data_con);

        ------------------------------------
        -- Verify the transfer succeeded
        ------------------------------------
        VALIDATE_REPLY(p_ctrl_con,TSFR_END_CODE,NULL,v_reply);

    EXCEPTION
    WHEN UTL_FILE.INVALID_PATH
    THEN
        UTL_TCP.CLOSE_CONNECTION(p_ctrl_con);
        RAISE_APPLICATION_ERROR(UTL_FILE_ERRCODE,'Directory '||v_localpath||' is not available to UTL_FILE.  Check the init.ora file for valid UTL_FILE directories.');

    WHEN UTL_FILE.INVALID_OPERATION
    THEN
        UTL_TCP.CLOSE_CONNECTION(p_ctrl_con);
        RAISE_APPLICATION_ERROR(UTL_FILE_ERRCODE,'The file '||v_filename||' in the directory '||v_localpath||' could not be opened for reading or writing.');

    WHEN UTL_FILE.READ_ERROR
    THEN
        UTL_TCP.CLOSE_CONNECTION(p_ctrl_con);
        RAISE_APPLICATION_ERROR(UTL_FILE_ERRCODE,'The system encountered an error while trying to read '||v_filename||' in the directory '||v_localpath);

    WHEN UTL_FILE.WRITE_ERROR
    THEN
        UTL_TCP.CLOSE_CONNECTION(p_ctrl_con);
        RAISE_APPLICATION_ERROR(UTL_FILE_ERRCODE,'The system encountered an error while trying to write to '||v_filename||' in the directory '||v_localpath);

    WHEN UTL_FILE.INTERNAL_ERROR
    THEN
        UTL_TCP.CLOSE_CONNECTION(p_ctrl_con);
        RAISE_APPLICATION_ERROR(UTL_FILE_ERRCODE,'The UTL_FILE package encountered an unexpected internal system error.');

    WHEN OTHERS
    THEN
        IF UTL_FILE.IS_OPEN(u_filehandle)
        THEN
            UTL_FILE.FCLOSE(u_filehandle);
        END IF;
        UTL_TCP.CLOSE_CONNECTION(u_data_con);

        RAISE;

    END TRANSFER_FILE;

    PROCEDURE TRANSFER_CLOB(u_ctrl_con          IN OUT UTL_TCP.CONNECTION,
                            p_clob              IN OUT CLOB,
                            p_filename          IN VARCHAR2,
                            p_pasv_host         IN VARCHAR2,
                            p_pasv_port         IN PLS_INTEGER,
                            p_transfer_mode     IN VARCHAR2,
                            v_status            OUT VARCHAR2,
                            v_error_message     OUT VARCHAR2,
                            n_bytes_transmitted OUT NUMBER,
                            d_trans_start       OUT DATE,
                            d_trans_end         OUT DATE)
    IS

        u_data_con    UTL_TCP.CONNECTION;
        v_tsfr_mode   VARCHAR2(3)      := p_transfer_mode;
        v_mode        VARCHAR2(1);
        v_tsfr_cmd    VARCHAR2(10);
        v_buffer      VARCHAR2(32767);
        v_filename    VARCHAR2(255)    := p_filename;
        v_host        VARCHAR2(20)     := p_pasv_host;
        n_port        PLS_INTEGER      := p_pasv_port;
        n_bytes       NUMBER;
        v_msg         VARCHAR2(255);
        v_reply       VARCHAR2(1000);
        v_err_status  VARCHAR2(20)     := 'ERROR';
        n_amt         BINARY_INTEGER   := 32767;
        n_offset      INTEGER          := 1;
        n_length      NUMBER;
        n_mod         NUMBER;
        n_sect        NUMBER;

    BEGIN

        ---------------------------------------------
        -- Initialize some of our OUT variables
        ---------------------------------------------
        n_bytes_transmitted := 0;

        ---------------------------------------------
        -- Initialize our CLOB processing variables
        ---------------------------------------------
        n_length := DBMS_LOB.GETLENGTH(p_clob);
        n_mod    := MOD(n_length,n_amt);
        n_sect   := CEIL(n_length/n_amt);

        IF UPPER(v_tsfr_mode) = 'PUT'
        THEN
            v_mode     := 'r';
            v_tsfr_cmd := 'STOR ';

        ELSIF UPPER(v_tsfr_mode) = 'GET'
        THEN
            v_mode     := 'w';
            v_tsfr_cmd := 'RETR ';

        END IF;

        ---------------------------------------------------------
        -- Open data connection on Passive host and port
        ---------------------------------------------------------
        u_data_con := UTL_TCP.OPEN_CONNECTION(v_host,n_port);

        -----------------------------------------------------------------------------
        -- Send the STOR or RETR command to send or receive the file
        -----------------------------------------------------------------------------
        n_bytes := UTL_TCP.WRITE_LINE(u_ctrl_con,v_tsfr_cmd||v_filename);
        VALIDATE_REPLY(u_ctrl_con,TSFR_START_CODE1,TSFR_START_CODE2,v_reply);

        IF UPPER(v_tsfr_mode) = 'PUT'
        THEN
            FOR i IN 1..n_sect
            LOOP
                IF i = n_sect
                THEN
                    n_amt := n_mod;

                END IF;

                DBMS_LOB.READ(p_clob,n_amt,n_offset,v_buffer);
                -- DBMS_OUTPUT.PUT_LINE('Buffer: '||v_buffer);
                n_bytes := UTL_TCP.WRITE_TEXT(u_data_con,v_buffer);
                n_bytes_transmitted := n_bytes_transmitted + n_bytes;
                n_offset := n_offset + n_amt;

            END LOOP;

        ELSIF UPPER(v_tsfr_mode) = 'GET'
        THEN
            LOOP
                BEGIN

                    v_buffer := UTL_TCP.GET_TEXT(u_data_con,32767);
                    DBMS_LOB.WRITEAPPEND(p_clob,LENGTH(v_buffer),v_buffer);
                    n_bytes_transmitted := n_bytes_transmitted + LENGTH(v_buffer);

                EXCEPTION
                WHEN UTL_TCP.END_OF_INPUT
                THEN
                    EXIT;
                END;

            END LOOP;

        END IF;

        d_trans_end := SYSDATE;

        -----------------------------------
        -- Close the Data Connection
        -----------------------------------
        UTL_TCP.CLOSE_CONNECTION(u_data_con);

        ------------------------------------
        -- Verify the transfer succeeded
        ------------------------------------
        VALIDATE_REPLY(u_ctrl_con,TSFR_END_CODE,NULL,v_reply);

    EXCEPTION
    WHEN OTHERS
    THEN
        RAISE;

    END TRANSFER_CLOB;

    /*
    PROCEDURE TRANSFER_BLOB(p_ctrl_con          IN OUT UTL_TCP.CONNECTION,
                            p_blob              IN OUT BLOB,
                            p_filename          IN VARCHAR2,
                            p_pasv_host         IN VARCHAR2,
                            p_pasv_port         IN PLS_INTEGER,
                            p_transfer_mode     IN VARCHAR2,
                            v_status            OUT VARCHAR2,
                            v_error_message     OUT VARCHAR2,
                            n_bytes_transmitted OUT NUMBER,
                            d_trans_start       OUT DATE,
                            d_trans_end         OUT DATE)
    IS

        u_data_con    UTL_TCP.CONNECTION;
        v_tsfr_mode   VARCHAR2(3)      := p_transfer_mode;
        v_mode        VARCHAR2(1);
        v_tsfr_cmd    VARCHAR2(10);
        v_buffer      RAW(32767);
        v_filename    VARCHAR2(255)    := p_filename;
        v_host        VARCHAR2(20)     := p_pasv_host;
        n_port        PLS_INTEGER      := p_pasv_port;
        n_bytes       NUMBER;
        v_msg         VARCHAR2(255);
        v_reply       VARCHAR2(1000);
        v_err_status  VARCHAR2(20)     := 'ERROR';
        n_amt         BINARY_INTEGER   := 32767;
        n_offset      INTEGER          := 1;
        n_length      NUMBER;
        n_mod         NUMBER;
        n_sect        NUMBER;

    BEGIN

        ---------------------------------------------
        -- Initialize some of our OUT variables
        ---------------------------------------------
        n_bytes_transmitted := 0;

        ---------------------------------------------
        -- Initialize our BLOB processing variables
        ---------------------------------------------
        n_length := DBMS_LOB.GETLENGTH(p_blob);
        n_mod    := MOD(n_length,n_amt);
        n_sect   := CEIL(n_length/n_amt);

        IF UPPER(v_tsfr_mode) = 'PUT'
        THEN
            v_mode     := 'r';
            v_tsfr_cmd := 'STOR ';

        ELSIF UPPER(v_tsfr_mode) = 'GET'
        THEN
            v_mode     := 'w';
            v_tsfr_cmd := 'RETR ';

        END IF;

        ---------------------------------------------------------
        -- Open data connection on Passive host and port
        ---------------------------------------------------------
        u_data_con := UTL_TCP.OPEN_CONNECTION(v_host,n_port);

        -----------------------------------------------------------------------------
        -- Send the STOR or RETR command to send or receive the file
        -----------------------------------------------------------------------------
        n_bytes := UTL_TCP.WRITE_LINE(p_ctrl_con,v_tsfr_cmd||v_filename);
        VALIDATE_REPLY(p_ctrl_con,TSFR_START_CODE1,TSFR_START_CODE2,v_reply);

        IF UPPER(v_tsfr_mode) = 'PUT'
        THEN
            FOR i IN 1..n_sect
            LOOP
                IF i = n_sect
                THEN
                    n_amt := n_mod;

                END IF;

                DBMS_LOB.READ(p_blob,n_amt,n_offset,v_buffer);
                -- DBMS_OUTPUT.PUT_LINE('Buffer: '||v_buffer);
                n_bytes := UTL_TCP.WRITE_RAW(u_data_con,v_buffer);
                n_bytes_transmitted := n_bytes_transmitted + n_bytes;
                n_offset := n_offset + n_amt;


            END LOOP;

        ELSIF UPPER(v_tsfr_mode) = 'GET'
        THEN
            LOOP
                BEGIN

                    v_buffer := UTL_TCP.GET_RAW(u_data_con,32767);
                    DBMS_LOB.WRITEAPPEND(p_blob,LENGTH(v_buffer),v_buffer);
                    n_bytes_transmitted := n_bytes_transmitted + LENGTH(v_buffer);

                EXCEPTION
                WHEN UTL_TCP.END_OF_INPUT
                THEN
                    EXIT;
                END;

            END LOOP;

        END IF;

        d_trans_end := SYSDATE;

        -----------------------------------
        -- Close the Data Connection
        -----------------------------------
        UTL_TCP.CLOSE_CONNECTION(u_data_con);

        ------------------------------------
        -- Verify the transfer succeeded
        ------------------------------------
        VALIDATE_REPLY(p_ctrl_con,TSFR_END_CODE,NULL,v_reply);

    END TRANSFER_BLOB;
    */

    PROCEDURE PUT(p_localpath         IN  VARCHAR2,
                  p_filename          IN  VARCHAR2,
                  p_hostname          IN  VARCHAR2,
                  p_username          IN  VARCHAR2,
                  p_password          IN  VARCHAR2,
                  p_remotepath        IN  VARCHAR2,
                  p_remotename        IN  VARCHAR2,
                  p_status            OUT VARCHAR2,
                  p_error_message     OUT VARCHAR2,
                  p_bytes_transmitted OUT NUMBER,
                  p_trans_start       OUT DATE,
                  p_trans_end         OUT DATE,
                  p_filetype          IN  VARCHAR2 DEFAULT 'BINARY',
                  p_port              IN  PLS_INTEGER DEFAULT 21)

    IS
        v_localpath         VARCHAR2(255)   := p_localpath;
        v_filename          VARCHAR2(255)   := p_filename;
        v_hostname 		    VARCHAR2(30) 	:= p_hostname;
        v_username 		    VARCHAR2(30) 	:= p_username;
        v_password 		    VARCHAR2(30) 	:= p_password;
        v_remotepath        VARCHAR2(255)   := p_remotepath;
        v_remotename        VARCHAR2(255)   := p_remotename;
        n_port			    PLS_INTEGER 	:= p_port;
        v_filetype          VARCHAR2(20)    := UPPER(p_filetype);
        v_transfer_mode     VARCHAR2(3)     := 'PUT';
        v_mode              VARCHAR2(1);
        u_ctrl_con		    UTL_TCP.CONNECTION;
        n_byte_count		PLS_INTEGER;
        n_first_index		NUMBER;
        v_msg			    VARCHAR2(250);
        v_reply			    VARCHAR2(1000);
        v_pasv_host		    VARCHAR2(20);
        n_pasv_port		    NUMBER;

    BEGIN

        --------------------------------------------------
        -- Assume the overall transfer will succeed
        --------------------------------------------------
        p_status        := FTP_SUCCESS;
        p_error_message := FTP_SUCCESS_MSG;

        u_ctrl_con  := LOGIN(v_hostname,v_username,v_password,n_port);

        --------------------------------------------------------------
        -- Change to the remotepath directory, if one is specified
        --------------------------------------------------------------
        IF v_remotepath IS NOT NULL
        THEN
            CWD(u_ctrl_con,v_remotepath);

        END IF;

        --------------------------------------------------------------
        -- Set ASCII (A) or BINARY (I) mode
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Setting transfer mode');
        SET_TRANSFER_MODE(u_ctrl_con,UPPER(v_filetype));

        --------------------------------------------------------------
        -- Get a Passive connection to use for data transfer
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Creating Passive');
        CREATE_PASSIVE(u_ctrl_con,v_pasv_host,n_pasv_port);

        -- DBMS_OUTPUT.PUT_LINE('Transferring File');
        TRANSFER_FILE(u_ctrl_con,
                      v_localpath,
                      v_filename,
                      v_remotename,
                      v_pasv_host,
                      n_pasv_port,
                      v_transfer_mode,
                      v_filetype,
                      p_bytes_transmitted,
                      p_trans_start,
                      p_trans_end);
        --------------------------------------------------------------
        -- Quit our connection
        --------------------------------------------------------------
        QUIT(u_ctrl_con);

    EXCEPTION
    WHEN OTHERS
    THEN
        p_status := FTP_ERROR;
        p_error_message := SQLERRM;
        UTL_TCP.CLOSE_CONNECTION(u_ctrl_con);
        RAISE;

    END PUT;

    PROCEDURE PUT(p_clob              IN  CLOB,
                  p_hostname          IN  VARCHAR2,
                  p_username          IN  VARCHAR2,
                  p_password          IN  VARCHAR2,
                  p_remotepath        IN  VARCHAR2,
                  p_remotename        IN  VARCHAR2,
                  p_status            OUT VARCHAR2,
                  p_error_message     OUT VARCHAR2,
                  p_bytes_transmitted OUT NUMBER,
                  p_trans_start       OUT DATE,
                  p_trans_end         OUT DATE,
                  p_filetype          IN  VARCHAR2 DEFAULT 'BINARY',
                  p_port              IN  PLS_INTEGER DEFAULT 21)
    IS
        v_hostname 		    VARCHAR2(30) 	:= p_hostname;
        v_username 		    VARCHAR2(30) 	:= p_username;
        v_password 		    VARCHAR2(30) 	:= p_password;
        v_remotepath        VARCHAR2(255)   := p_remotepath;
        v_remotename        VARCHAR2(255)   := p_remotename;
        n_port			    PLS_INTEGER 	:= p_port;
        v_filetype          VARCHAR2(20)    := UPPER(p_filetype);
        u_ctrl_con          UTL_TCP.CONNECTION;
        v_pasv_host		    VARCHAR2(20);
        n_pasv_port		    NUMBER;
        o_clob              CLOB            := p_clob;


    BEGIN
        --------------------------------------------------
        -- Assume the overall transfer will succeed
        --------------------------------------------------
        p_status        := FTP_SUCCESS;
        p_error_message := FTP_SUCCESS_MSG;

        u_ctrl_con  := LOGIN(v_hostname,v_username,v_password,n_port);

        --------------------------------------------------------------
        -- Change to the remotepath directory, if one is specified
        --------------------------------------------------------------
        IF v_remotepath IS NOT NULL
        THEN
            CWD(u_ctrl_con,v_remotepath);

        END IF;

        --------------------------------------------------------------
        -- Set ASCII (A) or BINARY (I) mode
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Setting transfer mode');
        SET_TRANSFER_MODE(u_ctrl_con,UPPER(v_filetype));

        --------------------------------------------------------------
        -- Get a Passive connection to use for data transfer
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Creating Passive');
        CREATE_PASSIVE(u_ctrl_con,v_pasv_host,n_pasv_port);

        -- DBMS_OUTPUT.PUT_LINE('Transferring CLOB');
        TRANSFER_CLOB(u_ctrl_con,
                      o_clob,
                      p_remotename,
                      v_pasv_host,
                      n_pasv_port,
                      'PUT',
                      p_status,
                      p_error_message,
                      p_bytes_transmitted,
                      p_trans_start,
                      p_trans_end);

        --------------------------------------------------------------
        -- Quit our connection
        --------------------------------------------------------------
        QUIT(u_ctrl_con);

    END PUT;

    /*
    PROCEDURE PUT(p_blob              IN  BLOB,
                  p_username          IN  VARCHAR2,
                  p_password          IN  VARCHAR2,
                  p_hostname          IN  VARCHAR2,
                  p_remotepath        IN  VARCHAR2,
                  p_remotename        IN  VARCHAR2,
                  p_status            OUT VARCHAR2,
                  p_error_message     OUT VARCHAR2,
                  p_bytes_transmitted OUT NUMBER,
                  p_trans_start       OUT DATE,
                  p_trans_end         OUT DATE,
                  p_filetype          IN  VARCHAR2 DEFAULT 'BINARY',
                  p_port              IN  PLS_INTEGER DEFAULT 21)
    IS
        v_hostname 		    VARCHAR2(30) 	:= p_hostname;
        v_username 		    VARCHAR2(30) 	:= p_username;
        v_password 		    VARCHAR2(30) 	:= p_password;
        v_remotepath        VARCHAR2(255)   := p_remotepath;
        v_remotename        VARCHAR2(255)   := p_remotename;
        n_port			    PLS_INTEGER 	:= p_port;
        v_filetype          VARCHAR2(20)    := UPPER(p_filetype);
        u_ctrl_con          UTL_TCP.CONNECTION;
        v_pasv_host		    VARCHAR2(20);
        n_pasv_port		    NUMBER;
        o_blob              BLOB            := p_blob;


    BEGIN
        --------------------------------------------------
        -- Assume the overall transfer will succeed
        --------------------------------------------------
        p_status        := FTP_SUCCESS;
        p_error_message := FTP_SUCCESS_MSG;

        u_ctrl_con  := LOGIN(v_hostname,v_username,v_password,n_port);

        --------------------------------------------------------------
        -- Change to the remotepath directory, if one is specified
        --------------------------------------------------------------
        IF v_remotepath IS NOT NULL
        THEN
            CWD(u_ctrl_con,v_remotepath);

        END IF;

        --------------------------------------------------------------
        -- Set ASCII (A) or BINARY (I) mode
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Setting transfer mode');
        SET_TRANSFER_MODE(u_ctrl_con,UPPER(v_filetype));

        --------------------------------------------------------------
        -- Get a Passive connection to use for data transfer
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Creating Passive');
        CREATE_PASSIVE(u_ctrl_con,v_pasv_host,n_pasv_port);

        -- DBMS_OUTPUT.PUT_LINE('Transferring BLOB');
        TRANSFER_BLOB(u_ctrl_con,
                      o_blob,
                      p_remotename,
                      v_pasv_host,
                      n_pasv_port,
                      'PUT',
                      p_status,
                      p_error_message,
                      p_bytes_transmitted,
                      p_trans_start,
                      p_trans_end);

        --------------------------------------------------------------
        -- Quit our connection
        --------------------------------------------------------------
        QUIT(u_ctrl_con);

    END PUT;
    */

    PROCEDURE GET(p_localpath         IN  VARCHAR2,
                  p_filename          IN  VARCHAR2,
                  p_hostname          IN  VARCHAR2,
                  p_username          IN  VARCHAR2,
                  p_password          IN  VARCHAR2,
                  p_remotepath        IN  VARCHAR2,
                  p_remotename        IN  VARCHAR2,
                  p_status            OUT VARCHAR2,
                  p_error_message     OUT VARCHAR2,
                  p_bytes_transmitted OUT NUMBER,
                  p_trans_start       OUT DATE,
                  p_trans_end         OUT DATE,
                  p_filetype          IN  VARCHAR2 DEFAULT 'BINARY',
                  p_port              IN  PLS_INTEGER DEFAULT 21)
    IS

        v_localpath         VARCHAR2(255)   := p_localpath;
        v_filename          VARCHAR2(255)   := p_filename;
        v_hostname 		    VARCHAR2(30) 	:= p_hostname;
        v_username 		    VARCHAR2(30) 	:= p_username;
        v_password 		    VARCHAR2(30) 	:= p_password;
        v_remotepath        VARCHAR2(255)   := p_remotepath;
        v_remotename        VARCHAR2(255)   := p_remotename;
        n_port			    PLS_INTEGER 	:= p_port;
        v_filetype          VARCHAR2(20)    := UPPER(p_filetype);
        v_transfer_mode     VARCHAR2(3)     := 'GET';
        u_ctrl_con		    UTL_TCP.CONNECTION;
        v_pasv_host		    VARCHAR2(20);
        n_pasv_port		    NUMBER;

    BEGIN

        --------------------------------------------------
        -- Assume the overall transfer will succeed
        --------------------------------------------------
        p_status        := FTP_SUCCESS;
        p_error_message := FTP_SUCCESS_MSG;

        u_ctrl_con  := LOGIN(v_hostname,v_username,v_password,n_port);

        --------------------------------------------------------------
        -- Change to the remotepath directory, if one is specified
        --------------------------------------------------------------
        IF v_remotepath IS NOT NULL
        THEN
            CWD(u_ctrl_con,v_remotepath);

        END IF;

        --------------------------------------------------------------
        -- Set ASCII (A) or BINARY (I) mode
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Setting transfer mode');
        SET_TRANSFER_MODE(u_ctrl_con,UPPER(v_filetype));

        --------------------------------------------------------------
        -- Get a Passive connection to use for data transfer
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Creating Passive');
        CREATE_PASSIVE(u_ctrl_con,v_pasv_host,n_pasv_port);

        -- DBMS_OUTPUT.PUT_LINE('Transferring File');
        IF v_filename IS NULL AND v_remotename IS NOT NULL
        THEN
            v_filename := v_remotename;

        END IF;
        TRANSFER_FILE(u_ctrl_con,
                      v_localpath,
                      v_filename,
                      v_remotename,
                      v_pasv_host,
                      n_pasv_port,
                      v_transfer_mode,
                      v_filetype,
                      p_bytes_transmitted,
                      p_trans_start,
                      p_trans_end);
        --------------------------------------------------------------
        -- Quit our connection
        --------------------------------------------------------------
        QUIT(u_ctrl_con);


    EXCEPTION
    WHEN OTHERS
    THEN
        p_status        := FTP_ERROR;
        p_error_message := SQLERRM;
        UTL_TCP.CLOSE_ALL_CONNECTIONS;
        RAISE;

    END GET;

    PROCEDURE GET(p_clob              OUT CLOB,
                  p_hostname          IN  VARCHAR2,
                  p_username          IN  VARCHAR2,
                  p_password          IN  VARCHAR2,
                  p_remotepath        IN  VARCHAR2,
                  p_remotename        IN  VARCHAR2,
                  p_status            OUT VARCHAR2,
                  p_error_message     OUT VARCHAR2,
                  p_bytes_transmitted OUT NUMBER,
                  p_trans_start       OUT DATE,
                  p_trans_end         OUT DATE,
                  p_filetype          IN  VARCHAR2 DEFAULT 'BINARY',
                  p_port              IN  PLS_INTEGER DEFAULT 21)
    IS

        v_hostname 		    VARCHAR2(30) 	:= p_hostname;
        v_username 		    VARCHAR2(30) 	:= p_username;
        v_password 		    VARCHAR2(30) 	:= p_password;
        v_remotepath        VARCHAR2(255)   := p_remotepath;
        v_remotename        VARCHAR2(255)   := p_remotename;
        n_port			    PLS_INTEGER 	:= p_port;
        v_filetype          VARCHAR2(20)    := UPPER(p_filetype);
        u_ctrl_con          UTL_TCP.CONNECTION;
        v_pasv_host		    VARCHAR2(20);
        n_pasv_port		    NUMBER;

    BEGIN

        --------------------------------------------------
        -- Assume the overall transfer will succeed
        --------------------------------------------------
        p_status        := FTP_SUCCESS;
        p_error_message := FTP_SUCCESS_MSG;
        DBMS_LOB.CREATETEMPORARY(p_clob,TRUE);

        u_ctrl_con  := LOGIN(v_hostname,v_username,v_password,n_port);

        --------------------------------------------------------------
        -- Change to the remotepath directory, if one is specified
        --------------------------------------------------------------
        IF v_remotepath IS NOT NULL
        THEN
            CWD(u_ctrl_con,v_remotepath);

        END IF;

        --------------------------------------------------------------
        -- Set ASCII (A) or BINARY (I) mode
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Setting transfer mode');
        SET_TRANSFER_MODE(u_ctrl_con,UPPER(v_filetype));

        --------------------------------------------------------------
        -- Get a Passive connection to use for data transfer
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Creating Passive');
        CREATE_PASSIVE(u_ctrl_con,v_pasv_host,n_pasv_port);

        -- DBMS_OUTPUT.PUT_LINE('Transferring CLOB');
        TRANSFER_CLOB(u_ctrl_con,
                      p_clob,
                      p_remotename,
                      v_pasv_host,
                      n_pasv_port,
                      'GET',
                      p_status,
                      p_error_message,
                      p_bytes_transmitted,
                      p_trans_start,
                      p_trans_end);

        --------------------------------------------------------------
        -- Quit our connection
        --------------------------------------------------------------
        QUIT(u_ctrl_con);

    END GET;

    /*****
    PROCEDURE GET(p_blob              OUT BLOB,
                  p_username          IN  VARCHAR2,
                  p_password          IN  VARCHAR2,
                  p_hostname          IN  VARCHAR2,
                  p_remotepath        IN  VARCHAR2,
                  p_remotename        IN  VARCHAR2,
                  p_status            OUT VARCHAR2,
                  p_error_message     OUT VARCHAR2,
                  p_bytes_transmitted OUT NUMBER,
                  p_trans_start       OUT DATE,
                  p_trans_end         OUT DATE,
                  p_filetype          IN  VARCHAR2 DEFAULT 'BINARY',
                  p_port              IN  PLS_INTEGER DEFAULT 21)
    IS

        v_hostname 		    VARCHAR2(30) 	:= p_hostname;
        v_username 		    VARCHAR2(30) 	:= p_username;
        v_password 		    VARCHAR2(30) 	:= p_password;
        v_remotepath        VARCHAR2(255)   := p_remotepath;
        v_remotename        VARCHAR2(255)   := p_remotename;
        n_port			    PLS_INTEGER 	:= p_port;
        v_filetype          VARCHAR2(20)    := UPPER(p_filetype);
        u_ctrl_con          UTL_TCP.CONNECTION;
        v_pasv_host		    VARCHAR2(20);
        n_pasv_port		    NUMBER;

    BEGIN

        --------------------------------------------------
        -- Assume the overall transfer will succeed
        --------------------------------------------------
        p_status        := FTP_SUCCESS;
        p_error_message := FTP_SUCCESS_MSG;
        DBMS_LOB.CREATETEMPORARY(p_blob,TRUE);

        u_ctrl_con  := LOGIN(v_hostname,v_username,v_password,n_port);

        --------------------------------------------------------------
        -- Change to the remotepath directory, if one is specified
        --------------------------------------------------------------
        IF v_remotepath IS NOT NULL
        THEN
            CWD(u_ctrl_con,v_remotepath);

        END IF;

        --------------------------------------------------------------
        -- Set ASCII (A) or BINARY (I) mode
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Setting transfer mode');
        SET_TRANSFER_MODE(u_ctrl_con,UPPER(v_filetype));

        --------------------------------------------------------------
        -- Get a Passive connection to use for data transfer
        --------------------------------------------------------------
        -- DBMS_OUTPUT.PUT_LINE('Creating Passive');
        CREATE_PASSIVE(u_ctrl_con,v_pasv_host,n_pasv_port);

        -- DBMS_OUTPUT.PUT_LINE('Transferring BLOB');
        TRANSFER_BLOB(u_ctrl_con,
                      p_blob,
                      p_remotename,
                      v_pasv_host,
                      n_pasv_port,
                      'GET',
                      p_status,
                      p_error_message,
                      p_bytes_transmitted,
                      p_trans_start,
                      p_trans_end);

        --------------------------------------------------------------
        -- Quit our connection
        --------------------------------------------------------------
        QUIT(u_ctrl_con);

    END GET;
    *********/

    PROCEDURE FTP_MULTIPLE(t_ftp_file IN OUT r_ftp_file,
                           p_hostname IN VARCHAR2,
                           p_username IN VARCHAR2 DEFAULT NULL,
                           p_password IN VARCHAR2 DEFAULT NULL,
                           p_mode     IN VARCHAR2 DEFAULT NULL,
                           p_port     IN PLS_INTEGER DEFAULT 21,
                           p_filetype IN VARCHAR2 DEFAULT 'BINARY')
    IS
    BEGIN
        NULL;
    --EXCEPTION
    END FTP_MULTIPLE;

    PROCEDURE FTP_MULTIPLE(t_ftp_clob IN OUT r_ftp_clob,
                           p_hostname IN VARCHAR2 DEFAULT NULL,
                           p_username IN VARCHAR2 DEFAULT NULL,
                           p_password IN VARCHAR2 DEFAULT NULL,
                           p_mode     IN VARCHAR2 DEFAULT NULL,
                           p_port     IN PLS_INTEGER DEFAULT 21,
                           p_filetype IN VARCHAR2 DEFAULT 'BINARY')
    IS
    BEGIN
        NULL;
    --EXCEPTION
    END FTP_MULTIPLE;

    /*
    PROCEDURE FTP_MULTIPLE(t_ftp_blob IN OUT r_ftp_blob,
                           p_hostname IN VARCHAR2 DEFAULT NULL,
                           p_username IN VARCHAR2 DEFAULT NULL,
                           p_password IN VARCHAR2 DEFAULT NULL,
                           p_mode     IN VARCHAR2 DEFAULT NULL,
                           p_port     IN PLS_INTEGER DEFAULT 21,
                           p_filetype IN VARCHAR2 DEFAULT 'BINARY')
    IS
    BEGIN
        NULL;
    EXCEPTION
    END FTP_MULTIPLE;
    */

END; 
/