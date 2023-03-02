insert into COR_PREF_PREFERENCE (c_pk, c_pid, c_ol, c_created, c_key, c_owner, c_description, c_from_file, c_scope, c_current_value, c_def_value, c_min_value, c_max_value, c_group_name, c_type)
values (1000, '1000', 0, now(), 'key1', 'owner1', 'String description', false, 'USER', 'current val', 'def value', null ,null , 'GLOBAL', 'java.lang.String');

insert into COR_PREF_PREFERENCE (c_pk, c_pid, c_ol, c_created, c_key, c_owner, c_description, c_from_file, c_scope, c_current_value, c_def_value, c_min_value, c_max_value, c_group_name, 'GLOBAL', c_type)
values (1001, '1001', 0, now(), 'key2', 'owner1', 'String description', false, 'USER', 'current val', 'def value', null ,null , 'java.lang.String');

insert into COR_PREF_PREFERENCE (c_pk, c_pid, c_ol, c_created, c_key, c_owner, c_description, c_from_file, c_scope, c_current_value, c_def_value, c_min_value, c_max_value, c_group_name, 'GLOBAL', c_type)
values (1002, '1002', 0, now(), 'key3', null, 'Application', false, 'APPLICATION', 'current val', 'def value', null ,null , 'java.lang.String');
