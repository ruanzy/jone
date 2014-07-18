DROP TABLE IF EXISTS seq;
CREATE TABLE seq (
    currid int(11) NOT NULL,
	tname varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
	id int(11) NOT NULL,
	username varchar(100) NOT NULL,
	pwd varchar(200) NOT NULL,
	state char(1) default '1' NOT NULL,
	regtime varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS resources;
CREATE TABLE resources (
	id int(11) NOT NULL,
	name varchar(20) NOT NULL,
	type char(1) default '1' NOT NULL,
	method varchar(100),
	url varchar(100),
	pid int(11) NOT NULL,
	icon varchar(20),
	path varchar(100) NOT NULL,
	flag char(1) default '1' NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS log;
CREATE TABLE log (
	id int(11) NOT NULL,
	operator varchar(20) NOT NULL,
	ip varchar(40) NOT NULL,
	time varchar(20) NOT NULL,
	method varchar(40) NOT NULL,
	result char(1) NOT NULL,
	memo varchar(1000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS role;
CREATE TABLE role (
	id int(11) NOT NULL,
	name varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS userrole;
CREATE TABLE userrole (
	userid int(11) NOT NULL,
	roleid int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS roleres;
CREATE TABLE roleres (
	roleid int(11) NOT NULL,
	resid int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS dic;
CREATE TABLE dic (
	id int(11) NOT NULL,
	name varchar(100) NOT NULL,
	val varchar(100) NOT NULL,
	type varchar(100) NOT NULL,
	memo varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into users values(-1,'admin','24182508933eb89c7950e9001e6a0da7',1,'2008-08-08 08:08:08');

insert into resources values ('1', '系统管理', '1', null, null, '0', null, '1', '1');
insert into resources values ('2', '用户管理', '2', null, 'view/user/userlist.html', '1', null, '1/2', '1');
insert into resources values ('3', '角色管理', '2', null, 'view/role/rolelist.html', '1', null, '1/3', '1');
insert into resources values ('4', '资源管理', '2', null, 'view/res/reslist.html', '1', null, '1/4', '1');
insert into resources values ('5', '数据字典', '2', null, 'view/dic/diclist.html', '1', null, '1/5', '1');
insert into resources values ('6', '日志管理', '2', null, 'view/log/log.html', '1', null, '1/6', '1');
insert into resources values ('7', '用户删除', '3', 'PmsService.deluser', 'pmsdeluser', '2', null, '1/2/7', '1');
insert into resources values ('8', '用户分配角色', '3', 'PmsService.setrole', 'pmssetrole', '2', null, '1/2/8', '1');
insert into resources values ('9', '角色增加', '3', 'PmsService.addrole', 'pmsaddrole', '3', null, '1/2/9', '1');
insert into resources values ('10', '角色分配资源', '3', 'PmsService.setres', 'pmssetres', '3', null, '1/2/10', '1');
insert into resources values ('11', '资源增加', '3', 'PmsService.addres', 'pmsaddres', '4', null, '1/4/11', '1');
insert into resources values ('12', '资源删除', '3', 'PmsService.delres', 'pmsdelres', '4', null, '1/4/12', '1');
insert into resources values ('13', '字典增加', '3', 'PmsService.adddic', 'pmsadddic', '5', null, '1/5/13', '1');
insert into resources values ('14', '字典删除', '3', 'PmsService.deldic', 'pmsdeldic', '5', null, '1/5/14', '1');
insert into resources values ('15', '用户增加', '3', 'PmsService.reg', 'pmsreg', '2', null, '1/2/15', '1');
insert into resources values ('16', '用户激活', '3', 'PmsService.activeuser', 'pmsactiveuser', '2', null, '1/2/16', '1');
insert into resources values ('17', '用户禁止', '3', 'PmsService.canceluser', 'pmscanceluser', '2', null, '1/2/17', '1');

insert into seq values(1000,'resources');
insert into seq values(2,'dic');
insert into dic values(1,'正常','1','userstate','userstate');
insert into dic values(2,'禁止','0','userstate','userstate');

insert into resources values(100,'测试模块',1,null,null,0,null,'100',1);
insert into resources values(101,'测试菜单',2,null,'test.jsp',100,null,'100/101',1);
insert into resources values(102,'增加',3,'TestService.add','testadd',101,null,'100/101/102',1);
insert into resources values(103,'修改',3,'TestService.mod','testmod',101,null,'100/101/103',1);
insert into resources values(104,'删除',3,'TestService.del','testdel',101,null,'100/101/104',1);