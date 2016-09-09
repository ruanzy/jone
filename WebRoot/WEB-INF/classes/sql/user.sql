select1
===
select * from users where 1=1
@if(isNotEmpty(name)){
and username = #{name}
@}
@if(isNotEmpty(age)){
and age = #{age}
@}
@if(isNotEmpty(ids)){
and state in (#{join(ids)})
@}


select2
===


select * from alarm_object where 1=1
@if(isNotEmpty(name)){
and name like #{name}
@}

select3
===
select * from user where status in (#{join(ids)})
