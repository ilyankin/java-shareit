DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS item_requests CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TYPE IF EXISTS booking_status CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    serial PRIMARY KEY,
    name  varchar(255)        NOT NULL,
    email varchar(512) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id           serial PRIMARY KEY,
    description  text                        NOT NULL,
    requester_id bigint REFERENCES users (id),
    created      timestamp without time zone NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id           serial PRIMARY KEY,
    name         varchar(255) NOT NULL,
    description  text         NOT NULL,
    is_available boolean      NOT NULL,
    owner_id     bigint REFERENCES users (id)
);

CREATE TYPE booking_status AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS bookings
(
    id         serial PRIMARY KEY,
    start_date timestamp without time zone NOT NULL,
    end_date   timestamp without time zone NOT NULL,
    item_id    bigint REFERENCES items (id),
    booker_id  bigint REFERENCES users (id),
    status     booking_status NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id        serial PRIMARY KEY,
    text      text   NOT NULL,
    item_id   bigint REFERENCES items (id),
    author_id bigint REFERENCES users (id),
    created   timestamp without time zone NOT NULL
);
