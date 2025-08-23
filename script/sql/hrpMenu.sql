-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538674319362, '考勤异常', '0', '1', 'attendanceExceptions', 'hrp/attendanceExceptions/index', 1, 0, 'C', '0', '0', 'hrp:attendanceExceptions:list', '#', 103, 1, sysdate(), null, null, '考勤异常菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538674319363, '考勤异常查询', 1959119538674319362, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceExceptions:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538674319364, '考勤异常新增', 1959119538674319362, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceExceptions:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538674319365, '考勤异常修改', 1959119538674319362, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceExceptions:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538674319366, '考勤异常删除', 1959119538674319362, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceExceptions:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538674319367, '考勤异常导出', 1959119538674319362, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceExceptions:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538091311106, '打卡记录', '0', '1', 'attendanceRecords', 'hrp/attendanceRecords/index', 1, 0, 'C', '0', '0', 'hrp:attendanceRecords:list', '#', 103, 1, sysdate(), null, null, '打卡记录菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538091311107, '打卡记录查询', 1959119538091311106, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceRecords:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538091311108, '打卡记录新增', 1959119538091311106, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceRecords:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538091311109, '打卡记录修改', 1959119538091311106, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceRecords:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538091311110, '打卡记录删除', 1959119538091311106, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceRecords:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119538091311111, '打卡记录导出', 1959119538091311106, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:attendanceRecords:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119537571217409, '跨店成本分摊设定', '0', '1', 'crossStoreCostAllocation', 'hrp/crossStoreCostAllocation/index', 1, 0, 'C', '0', '0', 'hrp:crossStoreCostAllocation:list', '#', 103, 1, sysdate(), null, null, '跨店成本分摊设定菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119537571217410, '跨店成本分摊设定查询', 1959119537571217409, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:crossStoreCostAllocation:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119537571217411, '跨店成本分摊设定新增', 1959119537571217409, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:crossStoreCostAllocation:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119537571217412, '跨店成本分摊设定修改', 1959119537571217409, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:crossStoreCostAllocation:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119537571217413, '跨店成本分摊设定删除', 1959119537571217409, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:crossStoreCostAllocation:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119537571217414, '跨店成本分摊设定导出', 1959119537571217409, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:crossStoreCostAllocation:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536912711681, '调货记录', '0', '1', 'inventoryTransfers', 'hrp/inventoryTransfers/index', 1, 0, 'C', '0', '0', 'hrp:inventoryTransfers:list', '#', 103, 1, sysdate(), null, null, '调货记录菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536912711682, '调货记录查询', 1959119536912711681, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:inventoryTransfers:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536912711683, '调货记录新增', 1959119536912711681, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:inventoryTransfers:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536912711684, '调货记录修改', 1959119536912711681, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:inventoryTransfers:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536912711685, '调货记录删除', 1959119536912711681, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:inventoryTransfers:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536912711686, '调货记录导出', 1959119536912711681, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:inventoryTransfers:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119527534247938, '正式请假申请', '0', '1', 'leaveApplications', 'hrp/leaveApplications/index', 1, 0, 'C', '0', '0', 'hrp:leaveApplications:list', '#', 103, 1, sysdate(), null, null, '正式请假申请菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119527534247939, '正式请假申请查询', 1959119527534247938, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveApplications:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119527534247940, '正式请假申请新增', 1959119527534247938, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveApplications:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119527534247941, '正式请假申请修改', 1959119527534247938, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveApplications:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119527534247942, '正式请假申请删除', 1959119527534247938, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveApplications:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119527534247943, '正式请假申请导出', 1959119527534247938, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveApplications:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119528440217601, '期望休假/排休记录', '0', '1', 'leaveRequests', 'hrp/leaveRequests/index', 1, 0, 'C', '0', '0', 'hrp:leaveRequests:list', '#', 103, 1, sysdate(), null, null, '期望休假/排休记录菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119528440217602, '期望休假/排休记录查询', 1959119528440217601, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveRequests:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119528440217603, '期望休假/排休记录新增', 1959119528440217601, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveRequests:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119528440217604, '期望休假/排休记录修改', 1959119528440217601, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveRequests:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119528440217605, '期望休假/排休记录删除', 1959119528440217601, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveRequests:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119528440217606, '期望休假/排休记录导出', 1959119528440217601, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:leaveRequests:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529031614465, '薪资单记录', '0', '1', 'payrollRecords', 'hrp/payrollRecords/index', 1, 0, 'C', '0', '0', 'hrp:payrollRecords:list', '#', 103, 1, sysdate(), null, null, '薪资单记录菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529031614466, '薪资单记录查询', 1959119529031614465, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollRecords:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529031614467, '薪资单记录新增', 1959119529031614465, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollRecords:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529031614468, '薪资单记录修改', 1959119529031614465, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollRecords:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529031614469, '薪资单记录删除', 1959119529031614465, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollRecords:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529031614470, '薪资单记录导出', 1959119529031614465, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollRecords:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529673342978, '薪资规则', '0', '1', 'payrollSettings', 'hrp/payrollSettings/index', 1, 0, 'C', '0', '0', 'hrp:payrollSettings:list', '#', 103, 1, sysdate(), null, null, '薪资规则菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529673342979, '薪资规则查询', 1959119529673342978, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollSettings:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529673342980, '薪资规则新增', 1959119529673342978, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollSettings:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529673342981, '薪资规则修改', 1959119529673342978, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollSettings:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529673342982, '薪资规则删除', 1959119529673342978, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollSettings:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119529673342983, '薪资规则导出', 1959119529673342978, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:payrollSettings:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530264739841, '排班修改记录', '0', '1', 'scheduleModifications', 'hrp/scheduleModifications/index', 1, 0, 'C', '0', '0', 'hrp:scheduleModifications:list', '#', 103, 1, sysdate(), null, null, '排班修改记录菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530264739842, '排班修改记录查询', 1959119530264739841, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleModifications:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530264739843, '排班修改记录新增', 1959119530264739841, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleModifications:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530264739844, '排班修改记录修改', 1959119530264739841, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleModifications:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530264739845, '排班修改记录删除', 1959119530264739841, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleModifications:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530264739846, '排班修改记录导出', 1959119530264739841, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleModifications:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530847748097, '每日人力需求', '0', '1', 'scheduleRequirements', 'hrp/scheduleRequirements/index', 1, 0, 'C', '0', '0', 'hrp:scheduleRequirements:list', '#', 103, 1, sysdate(), null, null, '每日人力需求菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530847748098, '每日人力需求查询', 1959119530847748097, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleRequirements:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530847748099, '每日人力需求新增', 1959119530847748097, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleRequirements:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530847748100, '每日人力需求修改', 1959119530847748097, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleRequirements:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530847748101, '每日人力需求删除', 1959119530847748097, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleRequirements:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119530847748102, '每日人力需求导出', 1959119530847748097, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:scheduleRequirements:export',       '#', 103, 1, sysdate(), null, null, '');


-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119531430756353, '排班', '0', '1', 'schedules', 'hrp/schedules/index', 1, 0, 'C', '0', '0', 'hrp:schedules:list', '#', 103, 1, sysdate(), null, null, '排班菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119531430756354, '排班查询', 1959119531430756353, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:schedules:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119531430756355, '排班新增', 1959119531430756353, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:schedules:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119531430756356, '排班修改', 1959119531430756353, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:schedules:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119531430756357, '排班删除', 1959119531430756353, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:schedules:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119531430756358, '排班导出', 1959119531430756353, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:schedules:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532022153217, '班别休息时段', '0', '1', 'shiftBreaks', 'hrp/shiftBreaks/index', 1, 0, 'C', '0', '0', 'hrp:shiftBreaks:list', '#', 103, 1, sysdate(), null, null, '班别休息时段菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532022153218, '班别休息时段查询', 1959119532022153217, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shiftBreaks:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532022153219, '班别休息时段新增', 1959119532022153217, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shiftBreaks:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532022153220, '班别休息时段修改', 1959119532022153217, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shiftBreaks:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532022153221, '班别休息时段删除', 1959119532022153217, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shiftBreaks:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532022153222, '班别休息时段导出', 1959119532022153217, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shiftBreaks:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532542246914, '班别设定', '0', '1', 'shifts', 'hrp/shifts/index', 1, 0, 'C', '0', '0', 'hrp:shifts:list', '#', 103, 1, sysdate(), null, null, '班别设定菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532542246915, '班别设定查询', 1959119532542246914, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shifts:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532542246916, '班别设定新增', 1959119532542246914, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shifts:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532542246917, '班别设定修改', 1959119532542246914, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shifts:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532542246918, '班别设定删除', 1959119532542246914, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shifts:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119532542246919, '班别设定导出', 1959119532542246914, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:shifts:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533192364033, '技能岗位', '0', '1', 'skills', 'hrp/skills/index', 1, 0, 'C', '0', '0', 'hrp:skills:list', '#', 103, 1, sysdate(), null, null, '技能岗位菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533192364034, '技能岗位查询', 1959119533192364033, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:skills:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533192364035, '技能岗位新增', 1959119533192364033, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:skills:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533192364036, '技能岗位修改', 1959119533192364033, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:skills:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533192364037, '技能岗位删除', 1959119533192364033, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:skills:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533192364038, '技能岗位导出', 1959119533192364033, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:skills:export',       '#', 103, 1, sysdate(), null, null, '');


-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533716652034, '分店特殊事件', '0', '1', 'storeEvents', 'hrp/storeEvents/index', 1, 0, 'C', '0', '0', 'hrp:storeEvents:list', '#', 103, 1, sysdate(), null, null, '分店特殊事件菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533716652035, '分店特殊事件查询', 1959119533716652034, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeEvents:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533716652036, '分店特殊事件新增', 1959119533716652034, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeEvents:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533716652037, '分店特殊事件修改', 1959119533716652034, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeEvents:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533716652038, '分店特殊事件删除', 1959119533716652034, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeEvents:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119533716652039, '分店特殊事件导出', 1959119533716652034, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeEvents:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119539513180161, '分店', '0', '1', 'stores', 'hrp/stores/index', 1, 0, 'C', '0', '0', 'hrp:stores:list', '#', 103, 1, sysdate(), null, null, '分店菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119539513180162, '分店查询', 1959119539513180161, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:stores:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119539513180163, '分店新增', 1959119539513180161, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:stores:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119539513180164, '分店修改', 1959119539513180161, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:stores:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119539513180165, '分店删除', 1959119539513180161, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:stores:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119539513180166, '分店导出', 1959119539513180161, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:stores:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534370963457, '分店支援关系', '0', '1', 'storeSupportRelations', 'hrp/storeSupportRelations/index', 1, 0, 'C', '0', '0', 'hrp:storeSupportRelations:list', '#', 103, 1, sysdate(), null, null, '分店支援关系菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534370963458, '分店支援关系查询', 1959119534370963457, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeSupportRelations:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534370963459, '分店支援关系新增', 1959119534370963457, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeSupportRelations:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534370963460, '分店支援关系修改', 1959119534370963457, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeSupportRelations:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534370963461, '分店支援关系删除', 1959119534370963457, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeSupportRelations:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534370963462, '分店支援关系导出', 1959119534370963457, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:storeSupportRelations:export',       '#', 103, 1, sysdate(), null, null, '');


-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534765228034, '员工可上班时段', '0', '1', 'userAvailability', 'hrp/userAvailability/index', 1, 0, 'C', '0', '0', 'hrp:userAvailability:list', '#', 103, 1, sysdate(), null, null, '员工可上班时段菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534765228035, '员工可上班时段查询', 1959119534765228034, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userAvailability:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534765228036, '员工可上班时段新增', 1959119534765228034, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userAvailability:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534765228037, '员工可上班时段修改', 1959119534765228034, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userAvailability:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534765228038, '员工可上班时段删除', 1959119534765228034, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userAvailability:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119534765228039, '员工可上班时段导出', 1959119534765228034, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userAvailability:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535415345154, '员工档案扩展', '0', '1', 'userProfile', 'hrp/userProfile/index', 1, 0, 'C', '0', '0', 'hrp:userProfile:list', '#', 103, 1, sysdate(), null, null, '员工档案扩展菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535415345155, '员工档案扩展查询', 1959119535415345154, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userProfile:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535415345156, '员工档案扩展新增', 1959119535415345154, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userProfile:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535415345157, '员工档案扩展修改', 1959119535415345154, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userProfile:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535415345158, '员工档案扩展删除', 1959119535415345154, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userProfile:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535415345159, '员工档案扩展导出', 1959119535415345154, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userProfile:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535935438850, '员工技能关联', '0', '1', 'userSkills', 'hrp/userSkills/index', 1, 0, 'C', '0', '0', 'hrp:userSkills:list', '#', 103, 1, sysdate(), null, null, '员工技能关联菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535935438851, '员工技能关联查询', 1959119535935438850, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userSkills:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535935438852, '员工技能关联新增', 1959119535935438850, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userSkills:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535935438853, '员工技能关联修改', 1959119535935438850, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userSkills:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535935438854, '员工技能关联删除', 1959119535935438850, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userSkills:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119535935438855, '员工技能关联导出', 1959119535935438850, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userSkills:export',       '#', 103, 1, sysdate(), null, null, '');

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536396812290, '员工跨店权限', '0', '1', 'userStoreAccess', 'hrp/userStoreAccess/index', 1, 0, 'C', '0', '0', 'hrp:userStoreAccess:list', '#', 103, 1, sysdate(), null, null, '员工跨店权限菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536396812291, '员工跨店权限查询', 1959119536396812290, '1',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userStoreAccess:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536396812292, '员工跨店权限新增', 1959119536396812290, '2',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userStoreAccess:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536396812293, '员工跨店权限修改', 1959119536396812290, '3',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userStoreAccess:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536396812294, '员工跨店权限删除', 1959119536396812290, '4',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userStoreAccess:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(1959119536396812295, '员工跨店权限导出', 1959119536396812290, '5',  '#', '', 1, 0, 'F', '0', '0', 'hrp:userStoreAccess:export',       '#', 103, 1, sysdate(), null, null, '');
