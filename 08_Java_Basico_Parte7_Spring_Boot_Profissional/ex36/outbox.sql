-- Tabela outbox gravada na mesma transação do pedido
CREATE TABLE outbox (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    aggregate_id BIGINT       NOT NULL,
    tipo         VARCHAR(60)  NOT NULL,
    payload      JSON         NOT NULL,
    criado_em    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    publicado_em DATETIME(6)  NULL,
    INDEX idx_outbox_pendentes (publicado_em, id)
) ENGINE = InnoDB;
