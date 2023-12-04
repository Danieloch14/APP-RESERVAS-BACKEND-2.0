ALTER TABLE public.resource DROP COLUMN duration;
ALTER TABLE public.reservation DROP COLUMN "name";
ALTER TABLE public.reservation ADD duration time NULL;
ALTER TABLE public.resource ADD name varchar(255) NULL;