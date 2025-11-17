
ハッシュ化済みパスワード（元："password"）
$2a$10$p.y.tUK5TSEG8qI10QzfsuvJLlMV4cAocdBudnfQwtdkUEowTG2ui

Dokerコンテナで起動中のPostgreSQLに接続（切断 Ctrl+P→Ctrl+Q）
docker exec -it sample-postgres psql -U appuser -d appdb

接続後、ハッシュ化済みパスワードを登録するSQL文
INSERT INTO users (username, password, role, enabled)
VALUES ('user',
        '$2a$10$p.y.tUK5TSEG8qI10QzfsuvJLlMV4cAocdBudnfQwtdkUEowTG2ui',
        'ROLE_USER',
        true);