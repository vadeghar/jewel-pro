create table item_master (id bigint not null auto_increment,
item_code varchar(50),
item_huid varchar(50),
item_metal varchar(100),
item_name varchar(100),
item_quality varchar(10),
mc decimal(19,2),
pcs integer not null,
stone_name varchar(100),
stone_pcs integer not null,
stone_weight decimal(19,3),
stone_wt_in_cts decimal(19,3),
tag_no varchar(255),
va_percentage decimal(19,2),
wastage_in_gms decimal(19,3),
weight decimal(19,3),
primary key (id));


create table estimation (
id bigint not null auto_increment,
estimation_Date_Ts datetime,
existing_item varchar(5),
tag_no varchar(10),
item_name varchar(100),
item_code varchar(50),
pcs integer not null,
stone_name varchar(100),

estimation_no varchar(255),
gold_rate decimal(19,2),
silver_rate decimal(19,2),
rate decimal(19,2),

item_weight decimal(19,3),
net_weight decimal(19,3),
net_weight_price decimal(19,2),

is_gst_estimation varchar(255),
s_gst_percentage double precision,
c_gst_percentage double precision,

stone_weight decimal(19,3),
stone_wt_in_cts decimal(19,3),
stone_price_per_ct decimal(19,2),
stone_price decimal(19,2),

va_percentage decimal(19,2),
va_weight decimal(19,3),
va_price decimal(19,2),
weight_incl_va_wt decimal(19,3),
default_mc_enabled varchar(255),
mc decimal(19,2),

total_price decimal(19,2),
total_price_incl_gst decimal(19,2),
total_price_incl_mc decimal(19,2),
total_price_incl_stn decimal(19,2),
total_price_incl_va decimal(19,2),

item_master_id bigint,
discount decimal(19,2),

grand_total_after_discount decimal(19,2),
primary key (id));

create table metal_rate (
id bigint not null auto_increment,
item_metal varchar(10),
last_update_at datetime,
last_updated_by varchar(45),
rate decimal(19,2),
primary key (id));

create table purchase
(id bigint not null auto_increment,
c_gst_amount decimal(19,2),
description varchar(255),
gross_weight decimal(19,3),
is_gst_purchase varchar(5),
last_updated_ts datetime,
mc_amount decimal(19,2),
metal_type varchar(8),
net_weight decimal(19,3),
payment_mode varchar(8),
pcs integer,
purchase_bill_no varchar(12),
purchase_date date,
purchase_type varchar(8),
purity varchar(6),
rate decimal(19,2),
s_gst_amount decimal(19,2),
sale_purity varchar(255),
stn_weight decimal(19,3),
effective_weight decimal(19,3),
supplier_name varchar(50),
total_purchase_amount decimal(19,2),
primary key (id))

create table supplier (
id bigint not null auto_increment,
location varchar(99),
name varchar(55),
primary key (id));

create table address (
id bigint not null auto_increment,
city varchar(50),
state varchar(50),
street varchar(50),
zip_code varchar(8),
primary key (id));

create table customer (
id bigint not null auto_increment,
email varchar(30),
first_name varchar(50),
last_name varchar(50),
phone varchar(10),
address_id bigint,
primary key (id));

create table worker (id bigint not null auto_increment,
area varchar(50),
name varchar(50),
phone varchar(15),
primary key (id));

create table work_order (
id bigint not null auto_increment,
advance_paid decimal(19,2),
balance decimal(19,2),
delivery_date datetime,
gross_weight decimal(19,3),
item_name varchar(150),
mc decimal(19,2),
net_weight decimal(19,3),
order_date datetime,
order_total decimal(19,3),
rate decimal(19,2),
va_or_wastage decimal(19,3),
customer_id bigint,
worker_id bigint,
primary key (id));
alter table work_order add constraint FKt0bw3op1441q7wxibmxsk5ekv foreign key (customer_id) references customer (id);
alter table work_order add constraint FKi36l9ysh77e5lxpeqntkymygo foreign key (worker_id) references worker (id);


create table purchase_item (
id bigint not null auto_increment,
code varchar(50),
huid varchar(50),
making_charge decimal(19,2),
name varchar(50),
pcs integer not null,
stn_cost_per_ct decimal(19,2),
stn_type varchar(50),
stn_weight decimal(19,3),
va_weight decimal(19,3),
weight decimal(19,3),
purchase_id bigint,
primary key (id));

alter table purchase_item add constraint FK1mncc5yaore1sibgpj3jc4a7u foreign key (purchase_id) references purchase (id)