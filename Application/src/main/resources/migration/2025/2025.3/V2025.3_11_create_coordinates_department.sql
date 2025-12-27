ALTER TABLE department
    ADD COLUMN latitude DOUBLE PRECISION,
ADD COLUMN longitude DOUBLE PRECISION;

ALTER TABLE department_aud
    ADD COLUMN latitude DOUBLE PRECISION,
ADD COLUMN longitude DOUBLE PRECISION;


UPDATE department SET latitude = 49.8224, longitude = 19.0469 WHERE city = 'Bielsko-Biała';
UPDATE department SET latitude = 53.1325, longitude = 23.1688 WHERE city = 'Białystok';
UPDATE department SET latitude = 52.5200, longitude = 13.4050 WHERE city = 'Berlin';
UPDATE department SET latitude = 53.1235, longitude = 18.0084 WHERE city = 'Bydgoszcz';
UPDATE department SET latitude = 50.8118, longitude = 19.1203 WHERE city = 'Częstochowa';
UPDATE department SET latitude = 54.3520, longitude = 18.6466 WHERE city = 'Gdańsk';
UPDATE department SET latitude = 54.5189, longitude = 18.5305 WHERE city = 'Gdynia';
UPDATE department SET latitude = 54.4641, longitude = 17.0285 WHERE city = 'Słupsk';
UPDATE department SET latitude = 54.6059, longitude = 18.8010 WHERE city = 'Hel';
UPDATE department SET latitude = 52.7368, longitude = 15.2288 WHERE city = 'Gorzów Wielkopolski';
UPDATE department SET latitude = 51.7611, longitude = 18.0910 WHERE city = 'Kalisz';
UPDATE department SET latitude = 50.8661, longitude = 20.6286 WHERE city = 'Kielce';
UPDATE department SET latitude = 50.4346, longitude = 16.6610 WHERE city = 'Kłodzko';
UPDATE department SET latitude = 50.0647, longitude = 19.9450 WHERE city = 'Kraków';
UPDATE department SET latitude = 54.1944, longitude = 16.1722 WHERE city = 'Koszalin';
UPDATE department SET latitude = 52.0833, longitude = 20.5833 WHERE city = 'Książenice';
UPDATE department SET latitude = 50.2945, longitude = 18.6714 WHERE city = 'Gliwice';
UPDATE department SET latitude = 50.2863, longitude = 19.1041 WHERE city = 'Sosnowiec';
UPDATE department SET latitude = 50.2649, longitude = 19.0238 WHERE city = 'Katowice';
UPDATE department SET latitude = 50.1372, longitude = 18.9664 WHERE city = 'Tychy';
UPDATE department SET latitude = 49.8397, longitude = 24.0297 WHERE city = 'Lwów';
UPDATE department SET latitude = 53.1125, longitude = 20.3830 WHERE city = 'Mława';
UPDATE department SET latitude = 51.7592, longitude = 19.4550 WHERE city = 'Łódź';
UPDATE department SET latitude = 49.6175, longitude = 20.7153 WHERE city = 'Nowy Sącz';
UPDATE department SET latitude = 50.6751, longitude = 17.9213 WHERE city = 'Opole';
UPDATE department SET latitude = 52.4064, longitude = 16.9252 WHERE city = 'Poznań';
UPDATE department SET latitude = 50.0755, longitude = 14.4378 WHERE city = 'Praga';
UPDATE department SET latitude = 50.0971, longitude = 18.5410 WHERE city = 'Rybnik';
UPDATE department SET latitude = 50.0412, longitude = 21.9991 WHERE city = 'Rzeszów';
UPDATE department SET latitude = 50.3170, longitude = 18.8596 WHERE city = 'Ruda Śląska';
UPDATE department SET latitude = 53.4285, longitude = 14.5528 WHERE city = 'Szczecin';
UPDATE department SET latitude = 53.0138, longitude = 18.5984 WHERE city = 'Toruń';
UPDATE department SET latitude = 52.2297, longitude = 21.0122 WHERE city = 'Warszawa';
UPDATE department SET latitude = 52.1743, longitude = 21.5724 WHERE city = 'Mińsk Mazowiecki';
UPDATE department SET latitude = 54.6872, longitude = 25.2797 WHERE city = 'Wilno';
UPDATE department SET latitude = 51.1079, longitude = 17.0385 WHERE city = 'Wrocław';
UPDATE department SET latitude = 45.8150, longitude = 15.9819 WHERE city = 'Zagrzeb';
UPDATE department SET latitude = 50.7230, longitude = 23.2510 WHERE city = 'Zamość';
UPDATE department SET latitude = 51.9356, longitude = 15.5062 WHERE city = 'Zielona Góra';
