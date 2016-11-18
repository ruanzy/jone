DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id int(11) NOT NULL,
  username varchar(40) NOT NULL,
  pwd varchar(200) NOT NULL,
  depart int(11) DEFAULT NULL,
  birth varchar(40) DEFAULT NULL,
  gender int(1) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  state char(1) NOT NULL DEFAULT '1',
  regtime varchar(20) NOT NULL,
  memo varchar(255) DEFAULT NULL
);

insert users values(1, 'admin', 'YWRtaW5fYWRtaW4=', 1, '2016-09-15', 1, 'email', '888888', '1', '2016-08-08 00:00:00', 'admin');