create table `plain_text` (
      `id` int(11) not null auto_increment,
      `text` varchar(100) not null,
      primary key(`id`)
);

insert into plain_text(`text`) values('apple');
insert into plain_text(`text`) values('banana');
insert into plain_text(`text`) values('carrot');
insert into plain_text(`text`) values('dessert');
insert into plain_text(`text`) values('egg');
insert into plain_text(`text`) values('fish');
insert into plain_text(`text`) values('goose');