DELETE FROM mn_user_group;
INSERT INTO mn_user_group(id, name, active) VALUES (1, 'Administrator', 'Y');
INSERT INTO mn_user_group(id, name, active) VALUES (2, 'User', 'Y');
INSERT INTO mn_user_group(id, name, active) VALUES (3, 'Marketing User', 'Y');

DELETE FROM mn_user;
INSERT INTO mn_user(id, first_name, last_name, username, password, email, active, user_group_id) VALUES (1, 'John', 'Doe', 'johndoe', '', 'johndoe@yahoo.com', 'Y', 1);
INSERT INTO mn_user(id, first_name, last_name, username, password, email, active, user_group_id) VALUES (2, 'Fulan', '', 'fulan', '', 'fulan@yahoo.com', 'Y', 2);

DELETE FROM mn_user_group_menu_permission;
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.system', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.user', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.usergroup', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings', 'N', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.system', 'N', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.user', 'N', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.usergroup', 'N', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (3, 'menu.settings', 'Y', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (3, 'menu.settings.system', 'Y', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (3, 'menu.settings.user', 'Y', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (3, 'menu.settings.usergroup', 'Y', 'N');