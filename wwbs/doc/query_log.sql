select * from MdmLog
select * from MdmLogStastics

select count(*) from AgentLog al, ControlLog cl
where al.sourceDevice_id = cl.userDeviceId_id
--347540
select count(*) from ControlLog
select count(*) from AgentLog
-4172-4676local2_hj

select * from AgentLog

select * from ControlLog


select * from ThirdPartyDept
select * from ProductDevice

select * from 
	DF_User user, 
	UserDevice device
where 
	device.isRegistered = 1 and 
	user.id=device.owner_id 




