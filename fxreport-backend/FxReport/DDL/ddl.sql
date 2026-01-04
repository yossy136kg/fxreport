create table fxreport.fx_ai_comment (
  id bigserial not null
  , base_ccy character(3) not null
  , quote_ccy character(3) not null
  , rate_date date not null
  , summary text not null
  , created_at timestamp(6) with time zone default now() not null
  , primary key (id)
);
create table fxreport.fx_ai_daily_report (
  id bigserial not null
  , base_ccy character(3) not null
  , report_date date not null
  , report_text text not null
  , created_at timestamp(6) with time zone default now() not null
  , primary key (id)
);
create table fxreport.fx_rate (
  id bigserial not null
  , base_ccy character(3) not null
  , quote_ccy character(3) not null
  , rate_date date not null
  , rate numeric(18, 6) not null
  , created_at timestamp(6) with time zone default now() not null
  , primary key (id)
);
create table fxreport.fx_rate_metrics (
  id bigserial not null
  , base_ccy character(3) not null
  , quote_ccy character(3) not null
  , rate_date date not null
  , close_rate numeric(18, 6) not null
  , diff_prev numeric(18, 6)
  , pct_prev numeric(12, 6)
  , ma7 numeric(18, 6)
  , trend_7d text
  , created_at timestamp(6) with time zone default now() not null
  , primary key (id)
);
create table fxreport.fx_rate_raw (
  id bigserial not null
  , base_ccy character(3) not null
  , rate_date date not null
  , payload jsonb not null
  , fetched_at timestamp(6) with time zone default now() not null
  , primary key (id)
);
