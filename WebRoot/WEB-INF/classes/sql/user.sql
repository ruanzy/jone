count
===
select count(1) from users where 1=1
@if(isNotEmpty(username)){
and username = #{username}
@}

list
===
select * from users where 1=1
@if(isNotEmpty(username)){
and username = #{username}
@}