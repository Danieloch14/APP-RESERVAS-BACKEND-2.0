/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     23/11/2023 14:21:50                          */
/*==============================================================*/
/*==============================================================*/
/* Table: location                                              */
/*==============================================================*/
create table location (
                          id_location          serial               not null,
                          id_region            int4                 null,
                          floor                numeric              null,
                          address              varchar(250)         null,
                          place                varchar(250)         null,
                          constraint pk_location primary key (id_location)
);

/*==============================================================*/
/* Index: location_pk                                           */
/*==============================================================*/
create unique index location_pk on location (
                                             id_location
    );

/*==============================================================*/
/* Index: r_region_location_fk                                  */
/*==============================================================*/
create  index r_region_location_fk on location (
                                                id_region
    );

/*==============================================================*/
/* Table: menu                                                  */
/*==============================================================*/
create table menu (
                      id_menu              serial               not null,
                      label                varchar(250)         not null,
                      parent_menu          numeric(3)           null,
                      "order"              numeric(3)           not null,
                      path                 varchar(250)         not null,
                      description          varchar(250)         null,
                      constraint pk_menu primary key (id_menu)
);

/*==============================================================*/
/* Index: menu_pk                                               */
/*==============================================================*/
create unique index menu_pk on menu (
                                     id_menu
    );

/*==============================================================*/
/* Table: personal_data                                         */
/*==============================================================*/
create table personal_data (
                               id_personal_data     serial               not null,
                               name                 varchar(250)         not null,
                               lastname             varchar(250)         not null,
                               address              varchar(250)         null,
                               cellphone            varchar(250)         null,
                               email                varchar(250)         not null,
                               company              varchar(250)         null,
                               constraint pk_personal_data primary key (id_personal_data)
);

/*==============================================================*/
/* Index: personal_data_pk                                      */
/*==============================================================*/
create unique index personal_data_pk on personal_data (
                                                       id_personal_data
    );

/*==============================================================*/
/* Table: region                                                */
/*==============================================================*/
create table region (
                        id_region            serial               not null,
                        name                 varchar(250)         null,
                        constraint pk_region primary key (id_region)
);

/*==============================================================*/
/* Index: region_pk                                             */
/*==============================================================*/
create unique index region_pk on region (
                                         id_region
    );

/*==============================================================*/
/* Table: reservation                                           */
/*==============================================================*/
create table reservation (
                             id_reservation       serial               not null,
                             id_resource          int4                 not null,
                             id_user              int4                 not null,
                             start_date           date                 not null,
                             end_date             date                 not null,
                             status               varchar(255)         not null,
                             description          varchar(250)         null,
                             constraint pk_reservation primary key (id_reservation)
);

/*==============================================================*/
/* Index: reservation_pk                                        */
/*==============================================================*/
create unique index reservation_pk on reservation (
                                                   id_reservation
    );

/*==============================================================*/
/* Index: r_resource_reservation_fk                             */
/*==============================================================*/
create  index r_resource_reservation_fk on reservation (
                                                        id_resource
    );

/*==============================================================*/
/* Index: r_user_reservation_fk                                 */
/*==============================================================*/
create  index r_user_reservation_fk on reservation (
                                                    id_user
    );

/*==============================================================*/
/* Table: resource                                              */
/*==============================================================*/
create table resource (
                          id_resource          serial               not null,
                          id_type_resource     int4                 null,
                          id_location          int4                 null,
                          res_id_resource      int4                 null,
                          cod_number           varchar(20)          not null,
                          capacity             numeric              null,
                          price                numeric(10,3)        null,
                          is_parking           bool                 not null,
                          path_images          varchar(250)         null,
                          is_availability      bool                 null,
                          constraint pk_resource primary key (id_resource)
);

/*==============================================================*/
/* Index: resource_pk                                           */
/*==============================================================*/
create unique index resource_pk on resource (
                                             id_resource
    );

/*==============================================================*/
/* Index: r_padre_hijo_fk                                       */
/*==============================================================*/
create  index r_padre_hijo_fk on resource (
                                           res_id_resource
    );

/*==============================================================*/
/* Index: r_type_resource_fk                                    */
/*==============================================================*/
create  index r_type_resource_fk on resource (
                                              id_type_resource
    );

/*==============================================================*/
/* Index: r_resource_region_fk                                  */
/*==============================================================*/
create  index r_resource_region_fk on resource (
                                                id_location
    );

/*==============================================================*/
/* Table: rol                                                   */
/*==============================================================*/
create table rol (
                     id_rol               serial               not null,
                     rol_name             varchar(255)         not null,
                     constraint pk_rol primary key (id_rol)
);

/*==============================================================*/
/* Index: rol_pk                                                */
/*==============================================================*/
create unique index rol_pk on rol (
                                   id_rol
    );

/*==============================================================*/
/* Table: r_menu_rol                                            */
/*==============================================================*/
create table r_menu_rol (
                            id_menu              int4                 not null,
                            id_rol               int4                 not null,
                            constraint pk_r_menu_rol primary key (id_menu, id_rol)
);

/*==============================================================*/
/* Index: r_menu_rol_pk                                         */
/*==============================================================*/
create unique index r_menu_rol_pk on r_menu_rol (
                                                 id_menu,
                                                 id_rol
    );

/*==============================================================*/
/* Index: r_menu_rol2_fk                                        */
/*==============================================================*/
create  index r_menu_rol2_fk on r_menu_rol (
                                            id_rol
    );

/*==============================================================*/
/* Index: r_menu_rol_fk                                         */
/*==============================================================*/
create  index r_menu_rol_fk on r_menu_rol (
                                           id_menu
    );

/*==============================================================*/
/* Table: r_user_menu                                           */
/*==============================================================*/
create table r_user_menu (
                             id_user              int4                 not null,
                             id_menu              int4                 not null,
                             constraint pk_r_user_menu primary key (id_user, id_menu)
);

/*==============================================================*/
/* Index: r_user_menu_pk                                        */
/*==============================================================*/
create unique index r_user_menu_pk on r_user_menu (
                                                   id_user,
                                                   id_menu
    );

/*==============================================================*/
/* Index: r_user_menu2_fk                                       */
/*==============================================================*/
create  index r_user_menu2_fk on r_user_menu (
                                              id_menu
    );

/*==============================================================*/
/* Index: r_user_menu_fk                                        */
/*==============================================================*/
create  index r_user_menu_fk on r_user_menu (
                                             id_user
    );

/*==============================================================*/
/* Table: type_resource                                         */
/*==============================================================*/
create table type_resource (
                               id_type_resource     serial               not null,
                               name                 varchar(250)         not null,
                               constraint pk_type_resource primary key (id_type_resource)
);

/*==============================================================*/
/* Index: type_resource_pk                                      */
/*==============================================================*/
create unique index type_resource_pk on type_resource (
                                                       id_type_resource
    );

/*==============================================================*/
/* Table: "user"                                                */
/*==============================================================*/
create table "user" (
                        id_personal_data     int4                 null,
                        id_user              serial               not null,
                        username             varchar(250)         not null,
                        password             varchar(250)         not null,
                        date_entry           date                 not null,
                        date_last_login      date                 null,
                        profile_picture      char(254)            null,
                        is_active            bool                 null,
                        is_not_locked        bool                 null,
                        constraint pk_user primary key (id_user)
);

/*==============================================================*/
/* Index: user_pk                                               */
/*==============================================================*/
create unique index user_pk on "user" (
                                       id_user
    );

/*==============================================================*/
/* Index: r_user_data_fk                                        */
/*==============================================================*/
create  index r_user_data_fk on "user" (
                                        id_personal_data
    );

alter table location
    add constraint fk_location_r_region__region foreign key (id_region)
        references region (id_region)
        on delete restrict on update restrict;

alter table reservation
    add constraint fk_reservat_r_resourc_resource foreign key (id_resource)
        references resource (id_resource)
        on delete restrict on update restrict;

alter table reservation
    add constraint fk_reservat_r_user_re_user foreign key (id_user)
        references "user" (id_user)
        on delete restrict on update restrict;

alter table resource
    add constraint fk_resource_r_padre_h_resource foreign key (res_id_resource)
        references resource (id_resource)
        on delete restrict on update restrict;

alter table resource
    add constraint fk_resource_r_resourc_location foreign key (id_location)
        references location (id_location)
        on delete restrict on update restrict;

alter table resource
    add constraint fk_resource_r_type_re_type_res foreign key (id_type_resource)
        references type_resource (id_type_resource)
        on delete restrict on update restrict;

alter table r_menu_rol
    add constraint fk_r_menu_r_r_menu_ro_menu foreign key (id_menu)
        references menu (id_menu)
        on delete restrict on update restrict;

alter table r_menu_rol
    add constraint fk_r_menu_r_r_menu_ro_rol foreign key (id_rol)
        references rol (id_rol)
        on delete restrict on update restrict;

alter table r_user_menu
    add constraint fk_r_user_m_r_user_me_user foreign key (id_user)
        references "user" (id_user)
        on delete restrict on update restrict;

alter table r_user_menu
    add constraint fk_r_user_m_r_user_me_menu foreign key (id_menu)
        references menu (id_menu)
        on delete restrict on update restrict;

alter table "user"
    add constraint fk_user_r_user_da_personal foreign key (id_personal_data)
        references personal_data (id_personal_data)
        on delete restrict on update restrict;

