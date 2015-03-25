DROP TABLE IF EXISTS seq;
CREATE TABLE seq (
    currid int(11) NOT NULL,
	tname varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
	id int(11) NOT NULL,
	username varchar(40) NOT NULL,
	pwd varchar(200) NOT NULL,
	depart int(11),
	birth varchar(40),
	gender int(1),
	email varchar(255),
	phone varchar(20),
	state char(1) default '1' NOT NULL,
	regtime varchar(20) NOT NULL,
	memo varchar(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS resources;
CREATE TABLE resources (
	id int(11) NOT NULL,
	name varchar(20) NOT NULL,
	type char(1) default '1' NOT NULL,
	method varchar(100),
	url varchar(100),
	pid int(11) NOT NULL,
	iconcls varchar(20),
	path varchar(100) NOT NULL,
	flag char(1) default '1' NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS log;
CREATE TABLE log (
	id int(11) NOT NULL,
	operator varchar(20) NOT NULL,
	ip varchar(40) NOT NULL,
	time varchar(20) NOT NULL,
	op varchar(40) NOT NULL,
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
	userid varchar(40) NOT NULL,
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

DROP TABLE IF EXISTS depart;
CREATE TABLE depart (
	id int(11) NOT NULL,
	name varchar(100) NOT NULL,
	--path varchar(255) NOT NULL,
	pid int(11) NOT NULL,
	isparent int(1) NOT NULL,
	memo varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS departemp;
CREATE TABLE departemp (
	departid int(11) NOT NULL,
	empid varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS api;
CREATE TABLE api (
	id int(11) NOT NULL,
	name varchar(100) NOT NULL,
	path varchar(100) NOT NULL,
	memo varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS apiparam;
CREATE TABLE apiparam (
	id int(11) NOT NULL,
	apiid int(11) NOT NULL,
	pname varchar(100) NOT NULL,
	required int(1) default 0,
	memo varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS warehouse;
CREATE TABLE warehouse (
	id int(11) NOT NULL,
	code varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	state int(1) default 0,
	memo varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS goods;
CREATE TABLE goods (
	id int(11) NOT NULL,
	category int(11) NOT NULL,
	name varchar(100) NOT NULL,
	spec varchar(255) NOT NULL,
	unit int(11) NOT NULL,
	purchase_price decimal(19,2) default 0.00,
	sale_price decimal(19,2) default 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS goods_category;
CREATE TABLE goods_category (
	id int(11) NOT NULL,
	name varchar(100) NOT NULL,
	pid int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS goods_unit;
CREATE TABLE goods_unit (
	id int(11) NOT NULL,
	name varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS supplier;
CREATE TABLE supplier (
	id int(11) NOT NULL,
	category int(11) NOT NULL,
	name varchar(100) NOT NULL,
	phone varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS supplier_category;
CREATE TABLE supplier_category (
	id int(11) NOT NULL,
	name varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
	id int(11) NOT NULL,
	--category int(11) NOT NULL,
	name varchar(100) NOT NULL,
	phone varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS customer_category;
CREATE TABLE customer_category (
	id int(11) NOT NULL,
	name varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS pbill;
CREATE TABLE purchase_bill (
	id int(11) NOT NULL,
	no varchar(100),
	supplier int(11) NOT NULL,
	warehouse int(11) NOT NULL,
	creator varchar(100),
	createtime varchar(100),
	state int(1) default 0,
	money decimal(19,2) default 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS pbill_detail;
CREATE TABLE pbill_detail (
	id int(11) NOT NULL,
	purchase_bill int(11) NOT NULL,
	goods int(11) NOT NULL,
	purchase_num int(11) NOT NULL,
	purchase_price decimal(19,2) default 0.00,
	purchase_money decimal(19,2) default 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `no` varchar(255) DEFAULT NULL,
  `supplier` int(11) DEFAULT NULL,
  `payment` decimal(19,2) DEFAULT 0.00,
  `payment_made` decimal(19,2) DEFAULT 0.00,
  `payment_due` decimal(19,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `payment_detail`;
CREATE TABLE `payment_detail` (
  `no` varchar(255) DEFAULT NULL,
  `paymoney` decimal(19,2) DEFAULT 0.00,
  `payuser` varchar(255) DEFAULT NULL,
  `paytime` varchar(100) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `receive`;
CREATE TABLE `receive` (
  `no` varchar(255) DEFAULT NULL,
  `customer` int(11) DEFAULT NULL,
  `receive` decimal(19,2) DEFAULT 0.00,
  `receive_made` decimal(19,2) DEFAULT 0.00,
  `receive_due` decimal(19,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `receive_detail`;
CREATE TABLE `receive_detail` (
  `no` varchar(255) DEFAULT NULL,
  `receivemoney` decimal(19,2) DEFAULT 0.00,
  `receiveuser` varchar(255) DEFAULT NULL,
  `receivetime` varchar(100) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS saleout;
CREATE TABLE saleout (
	id int(11) NOT NULL,
	no varchar(100),
	customer int(11) NOT NULL,
	warehouse int(11) NOT NULL,
	creator varchar(100),
	createtime varchar(100),
	state int(1) default 0,
	money decimal(19,2) default 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--insert into users values(-1,'admin','24182508933eb89c7950e9001e6a0da7',1,'2008-08-08 08:08:08');

INSERT INTO resources VALUES ('1', '系统管理', '1', null, null, '0', null, '1', '1');
INSERT INTO resources VALUES ('2', '用户管理', '2', null, 'view/user/userlist.html', '1', null, '1/2', '1');
INSERT INTO resources VALUES ('3', '角色管理', '2', null, 'view/role/rolelist.html', '1', null, '1/3', '1');
INSERT INTO resources VALUES ('4', '资源管理', '2', null, 'view/res/reslist.html', '1', null, '1/4', '1');
INSERT INTO resources VALUES ('5', '数据字典', '2', null, 'view/dic/diclist.html', '1', null, '1/5', '1');
INSERT INTO resources VALUES ('6', '日志管理', '2', null, 'view/log/log.html', '1', null, '1/6', '1');
INSERT INTO resources VALUES ('7', '用户删除', '3', 'PmsService.deluser', 'pmsdeluser', '2', null, '1/2/7', '1');
INSERT INTO resources VALUES ('8', '用户分配角色', '3', 'PmsService.setrole', 'pmssetrole', '2', null, '1/2/8', '1');
INSERT INTO resources VALUES ('9', '角色增加', '3', 'PmsService.addrole', 'pmsaddrole', '3', null, '1/2/9', '1');
INSERT INTO resources VALUES ('10', '角色分配资源', '3', 'PmsService.setres', 'pmssetres', '3', null, '1/2/10', '1');
INSERT INTO resources VALUES ('11', '资源增加', '3', 'PmsService.addres', 'pmsaddres', '4', null, '1/4/11', '1');
INSERT INTO resources VALUES ('12', '资源删除', '3', 'PmsService.delres', 'pmsdelres', '4', null, '1/4/12', '1');
INSERT INTO resources VALUES ('13', '字典增加', '3', 'PmsService.adddic', 'pmsadddic', '5', null, '1/5/13', '1');
INSERT INTO resources VALUES ('14', '字典删除', '3', 'PmsService.deldic', 'pmsdeldic', '5', null, '1/5/14', '1');
INSERT INTO resources VALUES ('15', '用户增加', '3', 'PmsService.reg', 'pmsreg', '2', null, '1/2/15', '1');
INSERT INTO resources VALUES ('16', '用户激活', '3', 'PmsService.activeuser', 'pmsactiveuser', '2', null, '1/2/16', '1');
INSERT INTO resources VALUES ('17', '用户禁止', '3', 'PmsService.canceluser', 'pmscanceluser', '2', null, '1/2/17', '1');
INSERT INTO resources VALUES ('18', '组织机构管理', '2', null, 'view/depart/list.html', '1', null, '1/18', '1');
INSERT INTO resources VALUES ('100', '测试模块', '1', null, null, '0', null, '100', '1');
INSERT INTO resources VALUES ('101', '测试菜单', '2', null, 'test.jsp', '100', null, '100/101', '1');
INSERT INTO resources VALUES ('102', '增加', '3', 'TestService.add', 'testadd', '101', null, '100/101/102', '1');
INSERT INTO resources VALUES ('103', '修改', '3', 'TestService.mod', 'testmod', '101', null, '100/101/103', '1');
INSERT INTO resources VALUES ('104', '删除', '3', 'TestService.del', 'testdel', '101', null, '100/101/104', '1');
INSERT INTO resources VALUES ('200', 'UI模块', '1', null, null, '0', null, '200', '1');
INSERT INTO resources VALUES ('201', '标签', '2', null, 'view/comp/label.html', '200', null, '200/201', '1');
INSERT INTO resources VALUES ('202', '按钮', '2', null, 'view/comp/button.html', '200', null, '200/202', '1');
INSERT INTO resources VALUES ('203', '对话框', '2', null, 'view/comp/msg.html', '200', null, '200/203', '1');
INSERT INTO resources VALUES ('300', '接口管理', '1', null, null, '0', null, '300', '1');
INSERT INTO resources VALUES ('301', '接口管理', '2', null, 'view/api/list.html', '300', null, '300/301', '1');
INSERT INTO resources VALUES ('1001', '仓库管理', '1', null, null, '0', '', '1001', '1');
INSERT INTO resources VALUES ('1002', '仓库管理', '2', null, 'view/warehouse/list.html', '1001', null, '1001/1002', '1');
INSERT INTO resources VALUES ('1003', '供应商管理', '1', null, null, '0', null, '1003', '1');
INSERT INTO resources VALUES ('1004', '供应商列表', '2', null, 'view/goods/supplier.html', '1003', null, '1003/1004', '1');
INSERT INTO resources VALUES ('1005', '客户管理', '1', null, null, '0', '', '1005', '1');
INSERT INTO resources VALUES ('1006', '客户列表', '2', null, 'view/customer/list.html', '1005', '', '1005/1006', '1');
INSERT INTO resources VALUES ('1007', '商品管理', '1', null, null, '0', 'a', '1007', '1');
INSERT INTO resources VALUES ('1008', '商品分类', '2', null, 'view/goods/category.html', '1007', '', '1007/1008', '1');
INSERT INTO resources VALUES ('1009', '计量单位', '2', null, 'view/goods/unit.html', '1007', '', '1007/1009', '1');
INSERT INTO resources VALUES ('1010', '商品列表', '2', null, 'view/goods/list.html', '1007', '', '1007/1010', '1');
INSERT INTO resources VALUES ('1011', '采购管理', '1', null, null, '0', '', '1011', '1');
INSERT INTO resources VALUES ('1012', '采购入库', '2', null, 'view/income/list.html', '1011', '', '1011/1012', '1');
INSERT INTO resources VALUES ('1013', '销售管理', '1', null, null, '0', '', '1013', '1');
INSERT INTO resources VALUES ('1014', '销售出库', '2', null, 'view/sale/out.html', '1013', '', '1013/1014', '1');
INSERT INTO resources VALUES ('1015', '销售退货', '2', null, 'view/sale/returned.html', '1013', '', '1013/1015', '1');
INSERT INTO resources VALUES ('1016', '应收应付', '1', null, null, '0', '', '1016', '1');
INSERT INTO resources VALUES ('1017', '应收款', '2', null, 'view/fund/receivable.html', '1016', '', '1016/1017', '1');
INSERT INTO resources VALUES ('1018', '应付款', '2', null, 'view/fund/payable.html', '1016', '', '1016/1018', '1');

insert into seq values(1000,'resources');
insert into seq values(2,'dic');
insert into dic values(1,'正常','1','userstate','userstate');
insert into dic values(2,'禁止','0','userstate','userstate');
insert into depart values(1,'总公司', 0, 1,'总公司');