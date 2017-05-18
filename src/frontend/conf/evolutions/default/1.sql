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
  keyword                       varchar(255),
  constraint pk_conference primary key (id)
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

create table user (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  password                      varchar(255),
  email                         varchar(255),
  security_question1            varchar(255),
  security_answer1              varchar(255),
  security_question2            varchar(255),
  security_answer2              varchar(255),
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

drop table if exists paper;

drop table if exists profile;

drop table if exists user;

