insert into cor_preference (c_pk, c_binvalue, c_description, c_float_value, c_from_file, c_maximum, c_minimum, c_value) values (1000, null, 'String value', null, false, null, null, 'String');
insert into cor_preference (c_pk, c_binvalue, c_description, c_float_value, c_from_file, c_maximum, c_minimum, c_value) values (1001, null, 'Integer value', 4711, false, 4700, 4712, null);

insert into cor_user_preference (c_key, c_owner, c_type, c_pk) VALUES ('1000', 'owner1', 'USER', 1000);
insert into cor_user_preference (c_key, c_owner, c_type, c_pk) VALUES ('1001', 'owner1', 'USER', 1001);
