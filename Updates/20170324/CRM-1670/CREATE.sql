
create table tq_crm.tqc_user_group_categories
(
  usr_code              number(8)               not null,
  usr_group_type        varchar2(50 byte),
  usr_type_sht_desc     varchar2(50 byte),
  usr_id_serial_format  varchar2(60 byte),
  usr_branch_code       number(8),
  usr_type_id           varchar2(3 byte)        not null,
  
  primary key (usr_code),
  constraint usr_type_sht_desc_uk unique (usr_type_sht_desc)
);

create sequence tq_crm.tqc_usr_grp_ctg_code_seq
  start with 1
  maxvalue 99999999
  minvalue 0
  nocycle
  nocache
  noorder;
  
create table tq_crm.tqc_group_category_users
(
  gtusr_code           number(8)                not null,
  gtusr_grpt_usr_code  number(8),
  gtuser_usr_code      number(8),
  gtusr_team_leader    varchar2(1 byte)         default 'N'                   not null,
  
  primary key (gtusr_code),
  constraint gtusr_grpt_usr_code_fk foreign key (gtusr_grpt_usr_code) references tq_crm.tqc_user_group_categories (usr_code),
  constraint gtuser_usr_code_fk foreign key (gtuser_usr_code) references tq_crm.tqc_users (usr_code)
);

create sequence tq_crm.tqc_grp_ctg_users_code_seq
  start with 1
  maxvalue 99999999
  minvalue 1
  nocycle
  cache 20
  noorder;  

create table tq_crm.tqc_group_category_accounts
(
  gtacc_code           number(8)                 not null,
  gtacc_grpt_usr_code  number(8),
  gtacc_acc_code       number(8),
  
  primary key (gtacc_code),
  constraint gtacc_grpt_usr_code_fk foreign key (gtacc_grpt_usr_code) references tq_crm.tqc_user_group_categories (usr_code),
  constraint gtacc_acc_code_fk foreign key (gtacc_acc_code) references tq_crm.tqc_accounts (acc_code)
);

create sequence tq_crm.tqc_grp_ctg_accounts_code_seq
  start with 1
  maxvalue 99999999
  minvalue 1
  nocycle
  cache 20
  noorder;