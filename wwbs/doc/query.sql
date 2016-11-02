
select * from _tree ;

select * from _tree t where t.root = 1 and t.lft >1 and t.rgt <8 order by t.lft;

update _tree t set t.lft = t.lft + 2 where t.root = 1 and t.lft > 2
update _tree t set t.rgt = t.rgt + 2 where t.root = 1 and t.rgt > 3

update _tree t set t.root = null where t.root = 1

delete t from _tree t where (t.root is null or t.root = 1) and t.lft >=14 and t.rgt <=15

