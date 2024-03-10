select * from movies."Actor";
select * from movies."Movie";

select * from movies."Actor" inner join movies."Movie" on movies."Actor".actor_id = movies."Movie".movie_id;
