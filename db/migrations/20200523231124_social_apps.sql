-- migrate:up
insert into config.social_app_type
values
('DISCORD'),
('FACEBOOK'),
('INSTAGRAM'),
('TWITCH'),
('TWITTER'),
('YOUTUBE');

-- migrate:down
delete from  config.social_app_type where social_app_type in 
( 'DISCORD', 'FACEBOOK', 'INSTAGRAM', 'TWITCH', 'TWITTER', 'YOUTUBE');
