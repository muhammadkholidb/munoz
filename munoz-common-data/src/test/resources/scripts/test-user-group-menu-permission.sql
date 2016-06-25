DELETE FROM mn_user_group;
INSERT INTO mn_user_group(id, name, active) VALUES (1, 'Administrator', 'Y');
INSERT INTO mn_user_group(id, name, active) VALUES (2, 'User', 'Y');

DELETE FROM mn_user_group_menu_permission;
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.system', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.user', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.userGroup', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings', 'N', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.system', 'N', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.user', 'N', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.userGroup', 'N', 'N');