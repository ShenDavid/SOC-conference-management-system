# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_company primary key (id)
);

create table computer (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  introduced                    datetime(6),
  discontinued                  datetime(6),
  company_id                    bigint,
  constraint pk_computer primary key (id)
);

create table conference (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  title                         varchar(255),
  location                      varchar(255),
  date                          varchar(255),
  status                        varchar(255),
  searchstatus                  varchar(255),
  ifreviewer                    varchar(255),
  ifadmin                       varchar(255),
  ifchair                       varchar(255),
  keyword                       varchar(255),
  constraint pk_conference primary key (id)
);

create table conference_detail (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  name                          varchar(255),
  url                           varchar(255),
  conference_email              varchar(255),
  chair_email                   varchar(255),
  tag_title                     varchar(255),
  config_content                varchar(255),
  can_pdf                       tinyint(1) default 0,
  can_postscript                tinyint(1) default 0,
  can_word                      tinyint(1) default 0,
  can_zip                       tinyint(1) default 0,
  can_multitopics               varchar(255),
  is_open_abstract              varchar(255),
  is_open_paper                 varchar(255),
  is_open_camera                varchar(255),
  is_blind_review               varchar(255),
  discuss_mode                  varchar(255),
  ballot_mode                   varchar(255),
  reviewer_number               varchar(255),
  is_mail_abstract              varchar(255),
  is_mail_upload                varchar(255),
  is_mail_review_submission     varchar(255),
  phase                         varchar(255),
  constraint pk_conference_detail primary key (id)
);

create table criteria (
  id                            bigint auto_increment not null,
  label                         varchar(255),
  explanations                  varchar(255),
  weight                        varchar(255),
  conferenceinfo                varchar(255),
  constraint pk_criteria primary key (id)
);

create table email_template (
  id                            bigint auto_increment not null,
  chair_name                    varchar(255),
  conference                    varchar(255),
  email_type                    varchar(255),
  subject                       varchar(255),
  template                      varchar(10000),
  constraint pk_email_template primary key (id)
);

create table pcmember (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  conference                    varchar(255),
  firstname                     varchar(255),
  lastname                      varchar(255),
  affiliation                   varchar(255),
  phone                         varchar(255),
  address                       varchar(255),
  if_chair                      varchar(255),
  if_reviewer                   varchar(255),
  constraint pk_pcmember primary key (id)
);

create table paper (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  title                         varchar(255),
  contactemail                  varchar(255),
  authors                       varchar(255),
  confirmemail                  varchar(255),
  firstname1                    varchar(255),
  lastname1                     varchar(255),
  email1                        varchar(255),
  affilation1                   varchar(255),
  firstname2                    varchar(255),
  lastname2                     varchar(255),
  email2                        varchar(255),
  affilation2                   varchar(255),
  firstname3                    varchar(255),
  lastname3                     varchar(255),
  email3                        varchar(255),
  affilation3                   varchar(255),
  firstname4                    varchar(255),
  lastname4                     varchar(255),
  email4                        varchar(255),
  affilation4                   varchar(255),
  firstname5                    varchar(255),
  lastname5                     varchar(255),
  email5                        varchar(255),
  affilation5                   varchar(255),
  firstname6                    varchar(255),
  lastname6                     varchar(255),
  email6                        varchar(255),
  affilation6                   varchar(255),
  firstname7                    varchar(255),
  lastname7                     varchar(255),
  email7                        varchar(255),
  affilation7                   varchar(255),
  otherauthor                   varchar(255),
  candidate                     varchar(255),
  volunteer                     varchar(255),
  paperabstract                 varchar(255),
  ifsubmit                      varchar(255),
  format                        varchar(255),
  papersize                     varchar(255),
  file                          varchar(255),
  conference                    varchar(255),
  topic                         varchar(255),
  status                        varchar(255),
  reviewstatus                  varchar(255),
  reviewerid                    bigint,
  review                        varchar(255),
  date                          varchar(255),
  constraint pk_paper primary key (id)
);

create table profile (
  userid                        bigint auto_increment not null,
  title                         varchar(255),
  research                      varchar(255),
  firstname                     varchar(255),
  lastname                      varchar(255),
  position                      varchar(255),
  affiliation                   varchar(255),
  email                         varchar(255),
  phone                         varchar(255),
  fax                           varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  country                       varchar(255),
  region                        varchar(255),
  zipcode                       bigint,
  comment                       varchar(255),
  constraint pk_profile primary key (userid)
);

create table review (
  id                            bigint auto_increment not null,
  paperid                       bigint,
  reviewerid                    bigint,
  iscriteria                    varchar(255),
  label                         varchar(255),
  review_content                varchar(255),
  reviewstatus                  varchar(255),
  constraint pk_review primary key (id)
);

create table review_question (
  id                            bigint auto_increment not null,
  question                      varchar(255),
  is_public                     varchar(255),
  conferenceinfo                varchar(255),
  list_of_choice1               varchar(255),
  position1                     varchar(255),
  list_of_choice2               varchar(255),
  position2                     varchar(255),
  list_of_choice3               varchar(255),
  position3                     varchar(255),
  list_of_choice4               varchar(255),
  position4                     varchar(255),
  list_of_choice5               varchar(255),
  position5                     varchar(255),
  list_of_choice6               varchar(255),
  position6                     varchar(255),
  list_of_choice7               varchar(255),
  position7                     varchar(255),
  constraint pk_review_question primary key (id)
);

create table status_code (
  id                            bigint auto_increment not null,
  label                         varchar(255),
  mailtemplate                  varchar(255),
  camerareadyrequired           varchar(255),
  conferenceinfo                varchar(255),
  constraint pk_status_code primary key (id)
);

create table topic (
  id                            bigint auto_increment not null,
  conference                    varchar(255),
  topic                         varchar(255),
  constraint pk_topic primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  password                      varchar(255),
  email                         varchar(255),
  security_question1            varchar(255),
  security_answer1              varchar(255),
  security_question2            varchar(255),
  security_answer2              varchar(255),
  privilege                     varchar(255),
  constraint pk_user primary key (id)
);

alter table computer add constraint fk_computer_company_id foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_computer_company_id on computer (company_id);


# --- !Downs

alter table computer drop foreign key fk_computer_company_id;
drop index ix_computer_company_id on computer;

drop table if exists company;

drop table if exists computer;

drop table if exists conference;

drop table if exists conference_detail;

drop table if exists criteria;

drop table if exists email_template;

drop table if exists pcmember;

drop table if exists paper;

drop table if exists profile;

drop table if exists review;

drop table if exists review_question;

drop table if exists status_code;

drop table if exists topic;

drop table if exists user;

