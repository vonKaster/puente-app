-- Seed roles
INSERT INTO roles(name) VALUES ('ADMIN'), ('USER');

-- Seed instruments (5 predefined financial instruments)
INSERT INTO instruments(symbol, name) VALUES
                                          ('AAPL','Apple Inc'),
                                          ('MSFT','Microsoft'),
                                          ('GOOGL','Alphabet Inc'),
                                          ('AMZN','Amazon'),
                                          ('META','Meta Platforms');
