/*
SQLyog Ultimate v12.4.0 (64 bit)
MySQL - 5.5.18 : Database - test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`test` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `test`;

/*Table structure for table `sync_task` */

DROP TABLE IF EXISTS `sync_task`;

CREATE TABLE `sync_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_type` varchar(50) NOT NULL COMMENT '任务类型',
  `task_desc` varchar(200) DEFAULT NULL COMMENT '任务描述',
  `task_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务状态(0 待处理 1 处理中 2 已处理 3 处理失败 99 处理异常)',
  `process_count` int(4) NOT NULL DEFAULT '0' COMMENT '任务执行次数',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `finish_date` datetime DEFAULT NULL COMMENT '完成时间',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8 COMMENT='异步任务';

/*Data for the table `sync_task` */

/*Table structure for table `sync_task_data` */

DROP TABLE IF EXISTS `sync_task_data`;

CREATE TABLE `sync_task_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` int(11) NOT NULL COMMENT '任务表主键',
  `task_data` varchar(2000) DEFAULT NULL COMMENT '任务数据',
  `data_index` tinyint(4) DEFAULT NULL COMMENT '截断顺序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COMMENT='同步任务数据';

/*Data for the table `sync_task_data` */

/*Table structure for table `sync_task_exception` */

DROP TABLE IF EXISTS `sync_task_exception`;

CREATE TABLE `sync_task_exception` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` int(11) NOT NULL COMMENT '任务表主键',
  `task_exception` varchar(2000) DEFAULT NULL COMMENT '异常信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务异常信息';

/*Data for the table `sync_task_exception` */

/*Table structure for table `sys_base_config` */

DROP TABLE IF EXISTS `sys_base_config`;

CREATE TABLE `sys_base_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '基础配置表主键',
  `biz_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务类型',
  `biz_key` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务配置键',
  `biz_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务配置值',
  `remark` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='系统配置表';

/*Data for the table `sys_base_config` */

/*Table structure for table `sys_job_config` */

DROP TABLE IF EXISTS `sys_job_config`;

CREATE TABLE `sys_job_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(100) NOT NULL COMMENT 'job名称',
  `job_class_bean_name` varchar(100) NOT NULL COMMENT 'bean名称',
  `sharding_total_count` int(4) NOT NULL DEFAULT '1' COMMENT '分片数',
  `sharding_item_params` varchar(500) DEFAULT NULL COMMENT '分配参数',
  `cron_expression` varchar(20) NOT NULL COMMENT 'cron表达式',
  `job_status` tinyint(3) NOT NULL COMMENT '状态(1 启动 0 停止)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user` varchar(30) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='调度任务配置';

/*Data for the table `sys_job_config` */

insert  into `sys_job_config`(`id`,`job_name`,`job_class_bean_name`,`sharding_total_count`,`sharding_item_params`,`cron_expression`,`job_status`,`remark`,`create_user`,`create_time`,`update_user`,`update_time`) values 
(3,'ExampleSyncTaskJob','ExampleSyncTaskJob',1,'0=taskType:SYNC_TEST','0/1 * * * * ?',0,'一片一线程，一线程执行一个任务类型的数据[一个任务类型对应一个业务处理方法]','test','2020-04-13 11:16:50','test','2020-05-08 14:36:15'),
(4,'HeartStatusJob','HeartStatusJob',2,'0=0,1=1','0/1 * * * * ?',1,'一片一线程，一线程执行JOB心跳状态监测','test','2020-04-20 15:14:32','test','2020-04-29 10:49:03'),
(6,'Example2SyncTaskJob','Example2SyncTaskJob',1,'0=taskType:SYNC_TEST2','0/1 * * * * ?',1,'一片一线程，一线程执行一个任务类型的数据[一个任务类型对应一个业务处理方法]','test','2020-05-08 14:31:20','test','2020-05-08 14:41:15');

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `skill` varchar(32) DEFAULT NULL COMMENT '技能',
  `evaluate` varchar(64) DEFAULT NULL COMMENT '评价',
  `fraction` bigint(11) DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

/*Data for the table `user_info` */

insert  into `user_info`(`id`,`name`,`age`,`skill`,`evaluate`,`fraction`) values 
(1,'Sans',18,'睡觉','Sans是一个爱睡觉,并且身材较矮骨骼巨大的骷髅小胖子',60),
(2,'papyrus',18,'JAVA','Papyrus是一个讲话大声、个性张扬的骷髅，给人自信、有魅力的骷髅小瘦子',58),
(3,'Sans',18,'睡觉','Sans是一个爱睡觉,并且身材较矮骨骼巨大的骷髅小胖子',60),
(4,'papyrus',18,'JAVA','Papyrus是一个讲话大声、个性张扬的骷髅，给人自信、有魅力的骷髅小瘦子',58);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
