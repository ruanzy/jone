create table if not exists users(
  username varchar(40) NOT NULL,
  pwd varchar(200) NOT NULL,
  depart integer  NULL,
  birth varchar(40)  NULL,
  isadmin TINYINT  NULL,
  email varchar(255)  NULL,
  phone varchar(20)  NULL,
  state char(1) NOT NULL,
  regtime varchar(20) NOT NULL,
  memo varchar(255)  NULL
);

insert into users values('admin', 'YWRtaW5fYWRtaW4=', 1, '2016-09-15', 1, 'email', '888888', 1, '2016-08-08 00:00:00', 'admin');