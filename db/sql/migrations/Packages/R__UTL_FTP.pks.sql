--
-- UTL_FTP  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.UTL_FTP
AUTHID CURRENT_USER
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

    /**
     * FTP File record datatype
     *
     * Elements
     * --------------------
     * localpath         - full directory name in which the local file resides or will reside
     *                     Windows: 'd:\oracle\utl_file'
     *                     UNIX: '/home/oracle/utl_file'
     * filename          - filename and extension for the file to be received or sent
     *                     Examples: 'myfile.dat' 'myfile20021119.xml'
     * OR
     * ftpclob           - CLOB containing data to be FTPd
     * OR
     * ftpblob           - BLOB containing data to be FTPd
     * username          - Username for the remote host
     * password          - password for the remote host
     * hostname          - remote host name or IP address
     *                     Examples: ftp.oracle.com, 148.87.36.11
     * remotepath        - full directory name in which the local file will be sent or the remote file exists.
     *                     Example: /usr/local/data
     * remotename        - name for the remote file
     * status            - status of the transfer.  'ERROR' or 'SUCCESS'
     * error_message     - meaningful (hopefully) error message explaining the reason for failure
     * bytes_transmitted - how many bytes were sent/received
     * trans_start       - date/time the transmission started
     * trans_end         - date/time the transmission ended
     * transfer_mode     - 'PUT' or 'GET'
     * filetype          - reserved for future use, ignored in code
     * port              - port used to connect to the host.
     *                     The default value of 21 should be correct 99% of the time.
     */

    TYPE r_ftp_file IS RECORD(localpath         VARCHAR2(255),
                              filename          VARCHAR2(255),
                              username          VARCHAR2(50),
                              PASSWORD          VARCHAR2(50),
                              hostname          VARCHAR2(255),
                              remotepath        VARCHAR2(255),
                              remotename        VARCHAR2(255),
                              status            VARCHAR2(20),
                              error_message     VARCHAR2(255),
                              bytes_transmitted NUMBER,
                              trans_start       DATE,
                              trans_end         DATE,
                              transfer_mode     VARCHAR2(3),
                              filetype          VARCHAR2(20) NOT NULL DEFAULT 'BINARY',
                              port              PLS_INTEGER  NOT NULL DEFAULT 21);

    TYPE r_ftp_clob IS RECORD(ftpclob           CLOB,
                              username          VARCHAR2(50),
                              PASSWORD          VARCHAR2(50),
                              hostname          VARCHAR2(255),
                              remotepath        VARCHAR2(255),
                              remotename        VARCHAR2(255),
                              status            VARCHAR2(20),
                              error_message     VARCHAR2(255),
                              bytes_transmitted NUMBER,
                              trans_start       DATE,
                              trans_end         DATE,
                              transfer_mode     VARCHAR2(3),
                              filetype          VARCHAR2(20) NOT NULL DEFAULT 'BINARY',
                              port              PLS_INTEGER  NOT NULL DEFAULT 21);

    TYPE r_ftp_blob IS RECORD(ftpblob           BLOB,
                              username          VARCHAR2(50),
                              PASSWORD          VARCHAR2(50),
                              hostname          VARCHAR2(255),
                              remotepath        VARCHAR2(255),
                              remotename        VARCHAR2(255),
                              status            VARCHAR2(20),
                              error_message     VARCHAR2(255),
                              bytes_transmitted NUMBER,
                              trans_start       DATE,
                              trans_end         DATE,
                              transfer_mode     VARCHAR2(3),
                              filetype          VARCHAR2(20) NOT NULL DEFAULT 'BINARY',
                              port              PLS_INTEGER  NOT NULL DEFAULT 21);


    /**
     * Table types for FTP_MULTIPLE procedures
     *
     */

    TYPE t_ftp_file IS TABLE OF r_ftp_file INDEX BY BINARY_INTEGER;
    TYPE t_ftp_clob IS TABLE OF r_ftp_clob INDEX BY BINARY_INTEGER;
    TYPE t_ftp_blob IS TABLE OF r_ftp_blob INDEX BY BINARY_INTEGER;

    UTL_FILE_EXCEPTION      EXCEPTION;
    FTP_EXCEPTION           EXCEPTION;
    FTP_EXCEPTION_ERRCODE   CONSTANT PLS_INTEGER := -20100;
    UTL_FILE_ERRCODE        CONSTANT PLS_INTEGER := -20101;
    PRAGMA EXCEPTION_INIT(FTP_EXCEPTION,-20100);
    PRAGMA EXCEPTION_INIT(UTL_FILE_EXCEPTION,-20101);

    /**
     *
     *
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
                  p_port              IN  PLS_INTEGER DEFAULT 21);

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
                  p_port              IN  PLS_INTEGER DEFAULT 21);

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
                  p_port              IN  PLS_INTEGER DEFAULT 21);
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
                  p_port              IN  PLS_INTEGER DEFAULT 21);

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
                  p_port              IN  PLS_INTEGER DEFAULT 21);

    /*
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
                  p_port              IN  PLS_INTEGER DEFAULT 21);
    */

    PROCEDURE FTP_MULTIPLE(t_ftp_file IN OUT r_ftp_file,
                           p_hostname IN VARCHAR2,
                           p_username IN VARCHAR2 DEFAULT NULL,
                           p_password IN VARCHAR2 DEFAULT NULL,
                           p_mode     IN VARCHAR2 DEFAULT NULL,
                           p_port     IN PLS_INTEGER DEFAULT 21,
                           p_filetype IN VARCHAR2 DEFAULT 'BINARY');

    PROCEDURE FTP_MULTIPLE(t_ftp_clob IN OUT r_ftp_clob,
                           p_hostname IN VARCHAR2 DEFAULT NULL,
                           p_username IN VARCHAR2 DEFAULT NULL,
                           p_password IN VARCHAR2 DEFAULT NULL,
                           p_mode     IN VARCHAR2 DEFAULT NULL,
                           p_port     IN PLS_INTEGER DEFAULT 21,
                           p_filetype IN VARCHAR2 DEFAULT 'BINARY');

    /*
    PROCEDURE FTP_MULTIPLE(t_ftp_blob IN OUT r_ftp_blob,
                           p_hostname IN VARCHAR2 DEFAULT NULL,
                           p_username IN VARCHAR2 DEFAULT NULL,
                           p_password IN VARCHAR2 DEFAULT NULL,
                           p_mode     IN VARCHAR2 DEFAULT NULL,
                           p_port     IN PLS_INTEGER DEFAULT 21,
                           p_filetype IN VARCHAR2 DEFAULT 'BINARY');
    */

END; 
/