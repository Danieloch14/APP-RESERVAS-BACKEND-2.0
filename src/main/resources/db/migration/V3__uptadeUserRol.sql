create table r_user_rol (
                            id_user              int4                 not null,
                            id_rol              int4                 not null,
                            constraint pk_r_user_rol primary key (id_user, id_rol)
);

/*==============================================================*/
/* Index: r_user_menu_pk                                        */
/*==============================================================*/
create unique index r_user_rol_pk on r_user_rol (
                                                 id_user,
                                                 id_rol
    );

/*==============================================================*/
/* Index: r_user_menu2_fk                                       */
/*==============================================================*/
create  index r_user_rol2_fk on r_user_rol (
                                            id_rol
    );

/*==============================================================*/
/* Index: r_user_menu_fk                                        */
/*==============================================================*/
create  index r_user_rol_fk on r_user_rol (
                                           id_user
    );
alter table r_user_rol
    add constraint fk_r_user_m_r_user_me_user foreign key (id_user)
        references "user" (id_user)
        on delete restrict on update restrict;

alter table r_user_rol
    add constraint fk_r_user_m_r_user_me_rol foreign key (id_rol)
        references rol (id_rol)
        on delete restrict on update restrict;
ALTER TABLE public.rol ADD description varchar(255) NULL;

ALTER TABLE public.r_menu_rol ADD permissions varchar(255) NULL;