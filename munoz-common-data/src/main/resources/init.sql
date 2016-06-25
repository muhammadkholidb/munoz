DELETE FROM mn_system;
INSERT INTO mn_system(key, value) VALUES ('languageCode', 'in');
INSERT INTO mn_system(key, value) VALUES ('templateCode', 'common');
INSERT INTO mn_system(key, value) VALUES ('image', 'sample-logo.png');
INSERT INTO mn_system(key, value) VALUES ('online', 'n');

DELETE FROM mn_user_group;
INSERT INTO mn_user_group(id, name, active) VALUES (1, 'Administrator', 'Y');
INSERT INTO mn_user_group(id, name, active) VALUES (2, 'User', 'Y');

DELETE FROM mn_user;
INSERT INTO mn_user(id, first_name, last_name, username, password, email, active, user_group_id) VALUES (1, 'John', 'Doe', 'johndoe', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'johndoe@yahoo.com', 'Y', 1);
INSERT INTO mn_user(id, first_name, last_name, username, password, email, active, user_group_id) VALUES (2, 'Fulan', '', 'fulan', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'fulan@yahoo.com', 'Y', 2);

DELETE FROM mn_user_group_menu_permission;
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.system', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.user', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (1, 'menu.settings.usergroup', 'Y', 'Y');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings', 'Y', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.system', 'Y', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.user', 'Y', 'N');
INSERT INTO mn_user_group_menu_permission(user_group_id, menu_code, view, modify) VALUES (2, 'menu.settings.usergroup', 'Y', 'N');
