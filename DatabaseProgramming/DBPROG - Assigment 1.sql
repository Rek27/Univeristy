/* 1. Create a stored procedure generate_driver_statistics that creates a table  (if not exists) that 
stores Year, Week, idDriver and the number of bookings. Furthermore, the procedure should 
compute the data (using an appropriate aggregate function) for a specified week and store it 
into the newly created table. As a parameter the year and the week of the year have to be 
handed over to the procedure.*/
delimiter $$
drop procedure if exists generate_driver_statistics$$
create procedure generate_driver_statistics(in year_ int, in week_ int)
driver_statistics:begin
	drop table if exists driver_statistics;
    -- Primary key consists of 3 columns because there may be 2 different weeks for the same driver
    -- I could have used the auto increment primary key as well
    create table driver_statistics(
		driverYear int not null,
        driverWeek int not null,
        idDriver int not null,
        numOfBookings int not null,
        constraint PK_Stat primary key (driverYear, driverWeek, idDriver)
	);
    
    -- if week is invalid, exit the procedure
    if week_ < 0 or week_ > 52 then
		select 'Invalid week number!';
	end if;
    
    -- taking the rows where BookingDate is in the given year/week and inserting them into the table
	insert into driver_statistics
    select year_, week_, Driver_idDriver, count(idBooking) from Booking B
    where YEAR(BookingTime) = year_ and WEEK(BookingTime) = week_
    group by year_, week_, Driver_idDriver;
        
end$$
delimiter ;
call generate_driver_statistics(2019, 22);



/* 2. Create an event exec_driver_statistics that executes the procedure described in assignment 1 
-- every Monday at 0:00 am. The event should start at October the 28th 2019. */
drop event if exists exec_driver_statistics;
delimiter $$
create event exec_driver_statistics
on schedule every 1 week
starts '2021-10-25 00:00:00'
do
	begin
		call generate_driver_statistics(YEAR(NOW()), WEEK(NOW()));
	end$$
delimiter ;


/* 3. Create a function customer_class that groups a given customer into the categories A, B or C 
depending on the distance she has travelled so far. The classes are as follows: 
‘A’ customers have a total distance that is at least 85% of the maximum distance 
'B’ customers have a distance  that  is  above  40%  of  the  average  distance  of  all 
customers. 
‘C’ customers neither fall into category ‘A’ nor into ‘B’.*/

drop function if exists customer_class;
delimiter $$
create function customer_class(idCustomer_ int)
returns char(1)
begin
	declare sumDistanceCustomer int;
    -- variable that stores given customer's sumDistance
    
      -- checking if the customer exists
	if not exists (select * from Customer where idCustomer = idCustomer_) then
		return '0';
	end if;
    
    set sumDistanceCustomer = (select SumDistance from travel_statistic where idCustomer = idCustomer_);
    
	case
	when sumDistanceCustomer >= 0.85 * (select MAX(SumDistance) from travel_statistic) then
		return 'A';
	when sumDistanceCustomer > 0.4 * (select AVG(SumDistance) from travel_statistic) then
		return 'B';
	else
		return 'C';
	end case;
end$$
delimiter ;