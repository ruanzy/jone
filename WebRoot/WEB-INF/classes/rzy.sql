/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : rzy

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2015-03-26 09:46:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for api
-- ----------------------------
DROP TABLE IF EXISTS `api`;
CREATE TABLE `api` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `path` varchar(100) NOT NULL,
  `memo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of api
-- ----------------------------
INSERT INTO `api` VALUES ('1', '用户列表', 'user/list', '用户列表');

-- ----------------------------
-- Table structure for apiparam
-- ----------------------------
DROP TABLE IF EXISTS `apiparam`;
CREATE TABLE `apiparam` (
  `id` int(11) NOT NULL,
  `pname` varchar(100) NOT NULL,
  `required` int(1) DEFAULT '0',
  `memo` varchar(100) DEFAULT NULL,
  `apiid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of apiparam
-- ----------------------------
INSERT INTO `apiparam` VALUES ('1', 'username', '1', '用户名', '1');
INSERT INTO `apiparam` VALUES ('2', 'phone', '0', '电话', '1');
INSERT INTO `apiparam` VALUES ('3', 'gender', '0', '性别(1:男,0:女)', '1');
INSERT INTO `apiparam` VALUES ('4', 'email', '0', '电子邮件', '1');
INSERT INTO `apiparam` VALUES ('5', 'birth', '0', '出生年月', '1');
INSERT INTO `apiparam` VALUES ('6', 'depart', '1', '部门', '1');
INSERT INTO `apiparam` VALUES ('7', 'state', '1', '状态', '1');
INSERT INTO `apiparam` VALUES ('8', 'memo', '0', '备注', '1');

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('2', '大连海恩橡胶辅机有限公司', '123');
INSERT INTO `customer` VALUES ('3', '哈尔滨工大建设监理有限公司', '456');
INSERT INTO `customer` VALUES ('4', '大连华城电子有限公司', '345');
INSERT INTO `customer` VALUES ('5', '大连宏光天宝大酒店有限公司', '234');
INSERT INTO `customer` VALUES ('6', 'customer', '3123');

-- ----------------------------
-- Table structure for customer_category
-- ----------------------------
DROP TABLE IF EXISTS `customer_category`;
CREATE TABLE `customer_category` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_category
-- ----------------------------

-- ----------------------------
-- Table structure for depart
-- ----------------------------
DROP TABLE IF EXISTS `depart`;
CREATE TABLE `depart` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `pid` int(11) NOT NULL,
  `isparent` int(1) NOT NULL,
  `memo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of depart
-- ----------------------------
INSERT INTO `depart` VALUES ('1', '总公司', '0', '1', '总公司');
INSERT INTO `depart` VALUES ('3', '上海公司', '1', '1', '上海公司');
INSERT INTO `depart` VALUES ('4', '南京公司', '1', '1', '南京公司');
INSERT INTO `depart` VALUES ('5', 'JAVA组', '3', '0', 'JAVA组');
INSERT INTO `depart` VALUES ('6', 'JAVA组', '4', '0', 'JAVA组');
INSERT INTO `depart` VALUES ('7', 'C组', '3', '0', 'C组');
INSERT INTO `depart` VALUES ('8', 'C组', '4', '0', 'C组');
INSERT INTO `depart` VALUES ('9', '系统组', '3', '0', '系统组');
INSERT INTO `depart` VALUES ('10', '系统组', '4', '0', '系统组');

-- ----------------------------
-- Table structure for departemp
-- ----------------------------
DROP TABLE IF EXISTS `departemp`;
CREATE TABLE `departemp` (
  `departid` int(11) NOT NULL,
  `empid` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of departemp
-- ----------------------------

-- ----------------------------
-- Table structure for dic
-- ----------------------------
DROP TABLE IF EXISTS `dic`;
CREATE TABLE `dic` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `val` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `memo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dic
-- ----------------------------
INSERT INTO `dic` VALUES ('1', '正常', '1', 'userstate', 'userstate');
INSERT INTO `dic` VALUES ('2', '禁止', '0', 'userstate', 'userstate');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL,
  `category` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `unit` int(11) NOT NULL,
  `spec` varchar(255) NOT NULL,
  `sale_price` decimal(19,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', '1', '创维酷开网络平板液晶电视', '1', '50英寸黑色K50J', '2899.00');
INSERT INTO `goods` VALUES ('2', '1', '康佳窄边全高清液晶电视', '1', '42英寸黑色LED42E330CE', '1999.00');

-- ----------------------------
-- Table structure for goods_category
-- ----------------------------
DROP TABLE IF EXISTS `goods_category`;
CREATE TABLE `goods_category` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods_category
-- ----------------------------
INSERT INTO `goods_category` VALUES ('1', '大家电');
INSERT INTO `goods_category` VALUES ('2', '机电');
INSERT INTO `goods_category` VALUES ('3', '大机电');
INSERT INTO `goods_category` VALUES ('4', '生活电器');

-- ----------------------------
-- Table structure for goods_unit
-- ----------------------------
DROP TABLE IF EXISTS `goods_unit`;
CREATE TABLE `goods_unit` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods_unit
-- ----------------------------
INSERT INTO `goods_unit` VALUES ('1', '台');
INSERT INTO `goods_unit` VALUES ('2', '箱');
INSERT INTO `goods_unit` VALUES ('3', '个');
INSERT INTO `goods_unit` VALUES ('4', '套');
INSERT INTO `goods_unit` VALUES ('5', '袋');
INSERT INTO `goods_unit` VALUES ('6', '盒');

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(11) NOT NULL,
  `operator` varchar(20) NOT NULL,
  `ip` varchar(40) NOT NULL,
  `time` varchar(20) NOT NULL,
  `op` varchar(40) NOT NULL,
  `method` varchar(40) NOT NULL,
  `result` char(1) NOT NULL,
  `memo` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `no` varchar(255) DEFAULT NULL,
  `supplier` int(11) DEFAULT NULL,
  `payment` decimal(9,2) DEFAULT NULL,
  `payment_made` decimal(9,2) DEFAULT NULL,
  `payment_due` decimal(9,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payment
-- ----------------------------
INSERT INTO `payment` VALUES ('P2015318000000', '2', '80000.00', '50000.00', '50000.00');

-- ----------------------------
-- Table structure for payment_detail
-- ----------------------------
DROP TABLE IF EXISTS `payment_detail`;
CREATE TABLE `payment_detail` (
  `no` varchar(255) DEFAULT NULL,
  `paymoney` decimal(9,2) DEFAULT NULL,
  `payuser` varchar(255) DEFAULT NULL,
  `paytime` varchar(100) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payment_detail
-- ----------------------------
INSERT INTO `payment_detail` VALUES ('P2015318000000', '20000.00', 'test', '2015-03-24', 'test');
INSERT INTO `payment_detail` VALUES ('P2015318000000', '10000.00', 'test', '2015-03-25', 'test');
INSERT INTO `payment_detail` VALUES ('P2015318000000', '10000.00', 'rzy', '2015-3-25', 'test');
INSERT INTO `payment_detail` VALUES ('P2015318000000', '10000.00', 'rzy', '2015-3-25', 'test');

-- ----------------------------
-- Table structure for pbill_detail
-- ----------------------------
DROP TABLE IF EXISTS `pbill_detail`;
CREATE TABLE `pbill_detail` (
  `id` int(11) NOT NULL,
  `purchase_bill` varchar(100) NOT NULL,
  `goods` int(11) NOT NULL,
  `purchase_num` int(11) NOT NULL,
  `purchase_price` decimal(19,2) DEFAULT '0.00',
  `purchase_money` decimal(19,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pbill_detail
-- ----------------------------
INSERT INTO `pbill_detail` VALUES ('1', 'P2015318000000', '2', '10', '1899.00', '18990.00');
INSERT INTO `pbill_detail` VALUES ('1', 'P2015318000000', '1', '1', '2799.00', '2799.00');
INSERT INTO `pbill_detail` VALUES ('1', 'P2015317000000', '1', '10', '2799.00', '27990.00');

-- ----------------------------
-- Table structure for purchase_bill
-- ----------------------------
DROP TABLE IF EXISTS `purchase_bill`;
CREATE TABLE `purchase_bill` (
  `id` int(11) NOT NULL,
  `no` varchar(100) DEFAULT NULL,
  `supplier` int(11) NOT NULL,
  `warehouse` int(11) NOT NULL,
  `creator` varchar(100) DEFAULT NULL,
  `createtime` varchar(100) DEFAULT NULL,
  `state` int(1) DEFAULT '0',
  `money` decimal(19,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of purchase_bill
-- ----------------------------
INSERT INTO `purchase_bill` VALUES ('1', 'P2015317000000', '1', '1', 'admin', '2015-03-18', '0', '60000.00');
INSERT INTO `purchase_bill` VALUES ('2', 'P2015318000000', '2', '2', 'admin', '2015-03-18', '1', '80000.00');

-- ----------------------------
-- Table structure for receive
-- ----------------------------
DROP TABLE IF EXISTS `receive`;
CREATE TABLE `receive` (
  `no` varchar(255) DEFAULT NULL,
  `customer` int(11) DEFAULT NULL,
  `receive` decimal(19,2) DEFAULT '0.00',
  `receive_made` decimal(19,2) DEFAULT '0.00',
  `receive_due` decimal(19,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receive
-- ----------------------------
INSERT INTO `receive` VALUES ('S2015317000000', '2', '60000.00', '10000.00', '0.00');

-- ----------------------------
-- Table structure for receive_detail
-- ----------------------------
DROP TABLE IF EXISTS `receive_detail`;
CREATE TABLE `receive_detail` (
  `no` varchar(255) DEFAULT NULL,
  `receivemoney` decimal(19,2) DEFAULT '0.00',
  `receiveuser` varchar(255) DEFAULT NULL,
  `receivetime` varchar(100) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of receive_detail
-- ----------------------------
INSERT INTO `receive_detail` VALUES ('S2015317000000', '10000.00', 'rzy', '2015-3-25', 'test');

-- ----------------------------
-- Table structure for resources
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `type` char(1) NOT NULL DEFAULT '1',
  `method` varchar(100) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `pid` int(11) NOT NULL,
  `iconcls` varchar(20) DEFAULT NULL,
  `path` varchar(100) NOT NULL,
  `flag` char(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resources
-- ----------------------------
INSERT INTO `resources` VALUES ('1', '系统管理', '1', null, null, '0', null, '1', '1');
INSERT INTO `resources` VALUES ('2', '用户管理', '2', null, 'view/user/userlist.html', '1', null, '1/2', '1');
INSERT INTO `resources` VALUES ('3', '角色管理', '2', null, 'view/role/rolelist.html', '1', null, '1/3', '1');
INSERT INTO `resources` VALUES ('4', '资源管理', '2', null, 'view/res/reslist.html', '1', null, '1/4', '1');
INSERT INTO `resources` VALUES ('5', '数据字典', '2', null, 'view/dic/diclist.html', '1', null, '1/5', '1');
INSERT INTO `resources` VALUES ('6', '日志管理', '2', null, 'view/log/log.html', '1', null, '1/6', '1');
INSERT INTO `resources` VALUES ('7', '用户删除', '3', 'PmsService.deluser', 'pmsdeluser', '2', null, '1/2/7', '1');
INSERT INTO `resources` VALUES ('8', '用户分配角色', '3', 'PmsService.setrole', 'pmssetrole', '2', null, '1/2/8', '1');
INSERT INTO `resources` VALUES ('9', '角色增加', '3', 'PmsService.addrole', 'pmsaddrole', '3', null, '1/2/9', '1');
INSERT INTO `resources` VALUES ('10', '角色分配资源', '3', 'PmsService.setres', 'pmssetres', '3', null, '1/2/10', '1');
INSERT INTO `resources` VALUES ('11', '资源增加', '3', 'PmsService.addres', 'pmsaddres', '4', null, '1/4/11', '1');
INSERT INTO `resources` VALUES ('12', '资源删除', '3', 'PmsService.delres', 'pmsdelres', '4', null, '1/4/12', '1');
INSERT INTO `resources` VALUES ('13', '字典增加', '3', 'PmsService.adddic', 'pmsadddic', '5', null, '1/5/13', '1');
INSERT INTO `resources` VALUES ('14', '字典删除', '3', 'PmsService.deldic', 'pmsdeldic', '5', null, '1/5/14', '1');
INSERT INTO `resources` VALUES ('15', '用户增加', '3', 'PmsService.reg', 'pmsreg', '2', null, '1/2/15', '1');
INSERT INTO `resources` VALUES ('16', '用户激活', '3', 'PmsService.activeuser', 'pmsactiveuser', '2', null, '1/2/16', '1');
INSERT INTO `resources` VALUES ('17', '用户禁止', '3', 'PmsService.canceluser', 'pmscanceluser', '2', null, '1/2/17', '1');
INSERT INTO `resources` VALUES ('18', '组织机构管理', '2', null, 'view/depart/list.html', '1', null, '1/18', '1');
INSERT INTO `resources` VALUES ('100', '测试模块', '1', null, null, '0', null, '100', '1');
INSERT INTO `resources` VALUES ('101', '测试菜单', '2', null, 'test.jsp', '100', null, '100/101', '1');
INSERT INTO `resources` VALUES ('102', '增加', '3', 'TestService.add', 'testadd', '101', null, '100/101/102', '1');
INSERT INTO `resources` VALUES ('103', '修改', '3', 'TestService.mod', 'testmod', '101', null, '100/101/103', '1');
INSERT INTO `resources` VALUES ('104', '删除', '3', 'TestService.del', 'testdel', '101', null, '100/101/104', '1');
INSERT INTO `resources` VALUES ('200', 'UI模块', '1', null, null, '0', null, '200', '1');
INSERT INTO `resources` VALUES ('201', '标签', '2', null, 'view/comp/label.html', '200', null, '200/201', '1');
INSERT INTO `resources` VALUES ('202', '按钮', '2', null, 'view/comp/button.html', '200', null, '200/202', '1');
INSERT INTO `resources` VALUES ('203', '对话框', '2', null, 'view/comp/msg.html', '200', null, '200/203', '1');
INSERT INTO `resources` VALUES ('300', '接口管理', '1', null, null, '0', null, '300', '1');
INSERT INTO `resources` VALUES ('301', '接口管理', '2', null, 'view/api/list.html', '300', null, '300/301', '1');
INSERT INTO `resources` VALUES ('1001', '仓库管理', '1', null, null, '0', '', '1001', '1');
INSERT INTO `resources` VALUES ('1002', '仓库管理', '2', null, 'view/warehouse/list.html', '1001', null, '1001/1002', '1');
INSERT INTO `resources` VALUES ('1003', '供应商管理', '1', null, null, '0', null, '1003', '1');
INSERT INTO `resources` VALUES ('1004', '供应商列表', '2', null, 'view/goods/supplier.html', '1003', null, '1003/1004', '1');
INSERT INTO `resources` VALUES ('1005', '客户管理', '1', null, null, '0', '', '1005', '1');
INSERT INTO `resources` VALUES ('1006', '客户管理', '2', null, 'view/customer/list.html', '1005', '', '1005/1006', '1');
INSERT INTO `resources` VALUES ('1007', '商品管理', '1', null, null, '0', 'a', '1007', '1');
INSERT INTO `resources` VALUES ('1008', '商品分类', '2', null, 'view/goods/category.html', '1007', '', '1007/1008', '1');
INSERT INTO `resources` VALUES ('1009', '计量单位', '2', null, 'view/goods/unit.html', '1007', '', '1007/1009', '1');
INSERT INTO `resources` VALUES ('1010', '商品列表', '2', null, 'view/goods/list.html', '1007', '', '1007/1010', '1');
INSERT INTO `resources` VALUES ('1011', '采购管理', '1', null, null, '0', '', '1011', '1');
INSERT INTO `resources` VALUES ('1012', '采购入库', '2', null, 'view/income/list.html', '1011', '', '1011/1012', '1');
INSERT INTO `resources` VALUES ('1013', '销售管理', '1', null, null, '0', '', '1013', '1');
INSERT INTO `resources` VALUES ('1014', '销售出库', '2', null, 'view/sale/out.html', '1013', '', '1013/1014', '1');
INSERT INTO `resources` VALUES ('1015', '销售退货', '2', null, 'view/sale/returned.html', '1013', '', '1013/1015', '1');
INSERT INTO `resources` VALUES ('1016', '应收应付', '1', null, null, '0', '', '1016', '1');
INSERT INTO `resources` VALUES ('1017', '应收款', '2', null, 'view/fund/receivable.html', '1016', '', '1016/1017', '1');
INSERT INTO `resources` VALUES ('1018', '应付款', '2', null, 'view/fund/payable.html', '1016', '', '1016/1018', '1');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'ruanzy');

-- ----------------------------
-- Table structure for roleres
-- ----------------------------
DROP TABLE IF EXISTS `roleres`;
CREATE TABLE `roleres` (
  `roleid` int(11) NOT NULL,
  `resid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roleres
-- ----------------------------
INSERT INTO `roleres` VALUES ('1', '1001');
INSERT INTO `roleres` VALUES ('1', '1002');
INSERT INTO `roleres` VALUES ('1', '1003');
INSERT INTO `roleres` VALUES ('1', '1004');
INSERT INTO `roleres` VALUES ('1', '1005');
INSERT INTO `roleres` VALUES ('1', '1006');
INSERT INTO `roleres` VALUES ('1', '1007');
INSERT INTO `roleres` VALUES ('1', '1008');
INSERT INTO `roleres` VALUES ('1', '1009');
INSERT INTO `roleres` VALUES ('1', '1010');
INSERT INTO `roleres` VALUES ('1', '1011');
INSERT INTO `roleres` VALUES ('1', '1012');
INSERT INTO `roleres` VALUES ('1', '1013');
INSERT INTO `roleres` VALUES ('1', '1014');
INSERT INTO `roleres` VALUES ('1', '1015');
INSERT INTO `roleres` VALUES ('1', '1016');
INSERT INTO `roleres` VALUES ('1', '1017');
INSERT INTO `roleres` VALUES ('1', '1018');

-- ----------------------------
-- Table structure for saleout
-- ----------------------------
DROP TABLE IF EXISTS `saleout`;
CREATE TABLE `saleout` (
  `id` int(11) NOT NULL,
  `no` varchar(100) DEFAULT NULL,
  `customer` int(11) NOT NULL,
  `warehouse` int(11) NOT NULL,
  `creator` varchar(100) DEFAULT NULL,
  `createtime` varchar(100) DEFAULT NULL,
  `state` int(1) DEFAULT '0',
  `money` decimal(19,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of saleout
-- ----------------------------
INSERT INTO `saleout` VALUES ('1', 'S2015317000000', '2', '1', 'test', '2015-03-17', '1', '52980.00');
INSERT INTO `saleout` VALUES ('2', 'S2015318000000', '4', '1', 'test', '2015-03-18', '0', '50000.00');

-- ----------------------------
-- Table structure for sale_detail
-- ----------------------------
DROP TABLE IF EXISTS `sale_detail`;
CREATE TABLE `sale_detail` (
  `id` int(11) NOT NULL,
  `no` varchar(100) DEFAULT NULL,
  `goods` int(11) NOT NULL,
  `sale_num` int(11) NOT NULL,
  `sale_price` decimal(19,2) DEFAULT '0.00',
  `sale_money` decimal(19,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sale_detail
-- ----------------------------
INSERT INTO `sale_detail` VALUES ('1', 'S2015317000000', '1', '10', '3099.00', '30990.00');
INSERT INTO `sale_detail` VALUES ('2', 'S2015317000000', '2', '10', '2199.00', '21990.00');

-- ----------------------------
-- Table structure for seq
-- ----------------------------
DROP TABLE IF EXISTS `seq`;
CREATE TABLE `seq` (
  `currid` int(11) NOT NULL,
  `tname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq
-- ----------------------------
INSERT INTO `seq` VALUES ('1018', 'resources');
INSERT INTO `seq` VALUES ('2', 'dic');
INSERT INTO `seq` VALUES ('10', 'depart');
INSERT INTO `seq` VALUES ('16', 'users');
INSERT INTO `seq` VALUES ('1', 'role');
INSERT INTO `seq` VALUES ('16', 'apiparam');
INSERT INTO `seq` VALUES ('6', 'warehouse');
INSERT INTO `seq` VALUES ('9', 'goodsunit');
INSERT INTO `seq` VALUES ('4', 'goods');
INSERT INTO `seq` VALUES ('6', 'customer');
INSERT INTO `seq` VALUES ('5', 'goods_category');
INSERT INTO `seq` VALUES ('3', 'supplier');

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES ('1', '苏宁易购大连分公司', '123');
INSERT INTO `supplier` VALUES ('2', '大连铭源伟业商贸有限公司', '456');

-- ----------------------------
-- Table structure for supplier_category
-- ----------------------------
DROP TABLE IF EXISTS `supplier_category`;
CREATE TABLE `supplier_category` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of supplier_category
-- ----------------------------
INSERT INTO `supplier_category` VALUES ('1', '电商');
INSERT INTO `supplier_category` VALUES ('2', '钢材');

-- ----------------------------
-- Table structure for userrole
-- ----------------------------
DROP TABLE IF EXISTS `userrole`;
CREATE TABLE `userrole` (
  `userid` varchar(40) NOT NULL,
  `roleid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userrole
-- ----------------------------
INSERT INTO `userrole` VALUES ('ruanzy', '1');
INSERT INTO `userrole` VALUES ('test', '1');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(40) NOT NULL,
  `pwd` varchar(200) NOT NULL,
  `depart` int(11) DEFAULT NULL,
  `birth` varchar(40) DEFAULT NULL,
  `gender` int(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `state` char(1) NOT NULL DEFAULT '1',
  `regtime` varchar(20) NOT NULL,
  `memo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'ruanzy', 'D8880FE779F6C9824D440B75BE0EB4AC', '6', '2000-08-08', '1', 'ruanzy@163.com', '15051886496', '1', '2015-03-12', 'ruanzy');
INSERT INTO `users` VALUES ('2', '123', '6E231BF0199DCC5AE2E37633BD5C0D6F', '8', '2015-03-12', '2', 'ruanzy@163.com', '15051886496', '0', '2015-03-12', null);
INSERT INTO `users` VALUES ('7', 'test', '00B1FE77AAE59298890A0B31ADE55694', '6', '2015-03-23', '1', 'test@163.com', '123', '1', '2015-03-23', 'test');
INSERT INTO `users` VALUES ('8', '1', '519A1F631C51DA39', '5', '2015-03-24', '1', '1', '1', '1', '2015-03-24', '1');
INSERT INTO `users` VALUES ('9', '2', '22580F18DECB6635', '3', '2015-03-24', '1', '2', '2', '1', '2015-03-24', '2');
INSERT INTO `users` VALUES ('10', '3', 'C13456C04800AB4F', '3', '2015-03-24', '1', '3', '3', '1', '2015-03-24', '3');
INSERT INTO `users` VALUES ('11', '5', '358CDBBEDC6A23BE', '6', '2015-03-24', '1', '5', '5', '1', '2015-03-24', '5');
INSERT INTO `users` VALUES ('12', '6', '3A756EBA1E536A4A', '7', '2015-03-24', '2', '6', '6', '1', '2015-03-24', '6');
INSERT INTO `users` VALUES ('13', '7', 'D8760F46E422DC8D', '3', '2015-03-24', '1', '7', '7', '1', '2015-03-24', '7');
INSERT INTO `users` VALUES ('14', '8', '2A7F69C2A536C8E4', '3', '2015-03-24', '2', '8', '8', '1', '2015-03-24', '8');
INSERT INTO `users` VALUES ('15', '9', '84618426DB25A1A1', '7', '2015-03-24', '2', '9', '9', '1', '2015-03-24', '9');
INSERT INTO `users` VALUES ('16', '567', '884D8FCB91CA61F7E2E37633BD5C0D6F', '4', '2015-03-24', '2', '5467', '5467', '1', '2015-03-24', '7');

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `id` int(11) NOT NULL,
  `code` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `state` int(1) DEFAULT '0',
  `memo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------
INSERT INTO `warehouse` VALUES ('2', '0002', '仓库2', '2', '仓库2');
INSERT INTO `warehouse` VALUES ('3', '0003', '仓库3', '1', '仓库3');
INSERT INTO `warehouse` VALUES ('1', '0001', '仓库1', '1', '仓库1');
