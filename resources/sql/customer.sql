count
===
select count(1) from customer where 1=1
@if(isNotEmpty(name)){
and name = #{name}
@}

list
===
select * from customer where 1=1
@if(isNotEmpty(name)){
and name = #{name}
@}