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

create table purchase (id bigint not null auto_increment,
c_gst_amount decimal(19,2),
description varchar(255),
gross_weight decimal(19,2),
mc_amount decimal(19,2),
metal_type varchar(255),
net_weight decimal(19,2),
payment_mode varchar(255), p
urchase_bill_no varchar(255),
purchase_date_ts datetime,
purchase_type varchar(255),
qty integer, rate decimal(19,2),
s_gst_amount decimal(19,2),
stn_weight decimal(19,2),
supplier_name varchar(255),
total_purchase_amount decimal(19,2),
primary key (id));


create table supplier (
id bigint not null auto_increment,
location varchar(99),
name varchar(55),
primary key (id));


