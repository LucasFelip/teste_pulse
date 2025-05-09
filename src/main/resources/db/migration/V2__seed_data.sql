INSERT INTO transportadoras (nome, frete_fixo) VALUES
                                                   ('Braspress', 15.00),
                                                   ('JadLog',    12.50),
                                                   ('FedEx',     25.00),
                                                   ('DHL',       30.00),
                                                   ('Correios',  10.00);

INSERT INTO produtos (nome, descricao, preco_unitario, estoque, ativo) VALUES
  ('Arroz Tipo 1',        'Pacote 5kg de arroz tipo 1',      20.00, 100, TRUE),
  ('Feijão Carioca',      'Pacote 1kg de feijão carioca',     8.50, 200, TRUE),
  ('Macarrão Espaguete',  'Pacote 500g de espaguete',         4.75, 150, TRUE),
  ('Açúcar Refinado',     'Pacote 1kg de açúcar refinado',    5.25, 120, TRUE),
  ('Café Torrado',        'Pacote 500g de café torrado',      18.00, 80, TRUE),
  ('Leite Integral',      'Caixa 1L de leite integral',       4.50, 300, TRUE),
  ('Óleo de Soja',        'Garrafa 900ml de óleo de soja',    7.20, 90, TRUE),
  ('Sal Refinado',        'Pacote 1kg de sal refinado',       2.50, 250, TRUE),
  ('Pão de Forma',        'Pacote com 12 fatias',             7.00, 110, TRUE);

-- 3. Clientes
INSERT INTO clientes (nome, email) VALUES
  ('Maria da Silva',     'maria.silva@example.com'),
  ('João Pereira',       'joao.pereira@example.com'),
  ('Ana Rodrigues',      'ana.rodrigues@example.com'),
  ('Carlos Alberto',     'carlos.alberto@example.com'),
  ('Fernanda Souza',     'fernanda.souza@example.com');

-- 4. Endereços
INSERT INTO enderecos (cliente_id, rua, numero, complemento, bairro, cidade, estado, cep, pais, is_principal) VALUES
  (1, 'Rua das Flores',       '123',  'Apto 12',      'Centro',         'São Paulo',    'SP', '01001-000', 'Brasil', TRUE),
  (1, 'Av. Paulista',         '1000', 'Sala 501',     'Bela Vista',     'São Paulo',    'SP', '01310-100', 'Brasil', FALSE),
  (2, 'Rua das Palmeiras',    '45',   NULL,           'Jardim',         'Curitiba',     'PR', '80010-000', 'Brasil', TRUE),
  (3, 'Av. Rio Branco',       '200',  'Loja A',       'Centro',         'Rio de Janeiro','RJ', '20040-001','Brasil', TRUE),
  (4, 'Rua XV de Novembro',   '800',  NULL,           'Centro',         'Curitiba',     'PR', '80020-310','Brasil', TRUE),
  (5, 'Al. Santos',           '1500', 'Cobertura',    'Cerqueira César','São Paulo',    'SP', '01418-200','Brasil', TRUE);

-- 5. Carrinhos (abertos)
INSERT INTO carrinhos (cliente_id, status, data_criacao) VALUES
  (1, 'ABERTO', NOW()),
  (2, 'ABERTO', NOW()),
  (3, 'ABERTO', NOW());

-- 6. Itens de Carrinho
INSERT INTO item_carrinho (carrinho_id, produto_id, quantidade, preco_unitario) VALUES
  (1, 1,  2, 20.00),
  (1, 4,  1,  5.25),
  (1, 6,  3,  4.50),
  (2, 2,  5,  8.50),
  (2, 9,  2,  7.00),
  (3, 3, 10,  4.75);

-- 7. Pedidos Fechados
INSERT INTO pedidos (cliente_id, carrinho_entrega_id, transportadora_id, frete, forma_pagamento, valor_total, data_pedido, status) VALUES
  (4, 5, 1, 15.00, 'CARTAO',  200.00, NOW() - INTERVAL '5 days', 'PAGO'),
  (5, 6, 3, 25.00, 'PIX',     350.00, NOW() - INTERVAL '2 days', 'PAGO');

-- 8. Itens de Pedido
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
  -- Pedido 1 (cliente 4)
  (1, 1, 3, 20.00),
  (1, 5, 2, 18.00),
  (1, 9, 1, 7.00),
  -- Pedido 2 (cliente 5)
  (2, 2, 10, 8.50),
  (2, 3, 4, 4.75),
  (2, 7, 2, 7.20);

-- 9. Notas Fiscais
INSERT INTO notas_fiscais (pedido_id, numero, data_emissao, json_nota) VALUES
  (1, UUID_GENERATE_V4()::text, NOW() - INTERVAL '5 days',
     '{"pedido":1,"valor_total":200.00,"data":"' || (NOW() - INTERVAL '5 days')::text || '"}'),
  (2, UUID_GENERATE_V4()::text, NOW() - INTERVAL '2 days',
     '{"pedido":2,"valor_total":350.00,"data":"' || (NOW() - INTERVAL '2 days')::text || '"}');
