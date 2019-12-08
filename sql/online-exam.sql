SET NAMES utf8mb4;
SET GLOBAL time_zone = '+8:00'; #修改mysql全局时区为北京时间,即我们所在的东8区
SET time_zone = '+8:00'; #修改当前会话时区
flush privileges; #立即生效

CREATE SCHEMA `online_exam` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

use online_exam;

#指定系统管理员
INSERT INTO online_exam.login_users( username, password, role ) VALUES ( "admin", "admin", 2 );

