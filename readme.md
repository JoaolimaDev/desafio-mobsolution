# Sistema de Agendamento Eventos

Este projeto é uma aplicação completa para gerenciamento de eventose e participantes, focada em um sistema de agendamento. A ideia central é oferecer uma interface robusta e moderna que integra funcionalidades essenciais para o controle de contatos, permitindo a comunicação eficiente entre o front-end e o back-end.

- **Tecnologias utilizadas:**
  -  JAVA 17.0.14
  -  DOCKER 27.5.1
  -  SPRING BOOT
  -  POSTGRESQL
  -  SWAGGER
  -  JOINFACES
  -  PRIMEFACES


- **Banco de Dados:**  


  ```sql
  create schema desafio;
    CREATE TABLE public.tb_presenca (
	"data" date NOT NULL,
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	participante_id int8 NULL,
	CONSTRAINT tb_presenca_participante_id_data_key UNIQUE (participante_id, data),
	CONSTRAINT tb_presenca_pkey PRIMARY KEY (id),
	CONSTRAINT ukl1f98abvq955l22wsjpl0ov3h UNIQUE (participante_id, data),
	CONSTRAINT fkj3a1adc5qjmp0nj9jal0kcho5 FOREIGN KEY (participante_id) REFERENCES public.tb_participante(id)
    );
    CREATE TABLE public.tb_participante (
	evento_id int8 NULL,
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	cpf varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	nome varchar(255) NOT NULL,
	CONSTRAINT tb_participante_cpf_key UNIQUE (cpf),
	CONSTRAINT tb_participante_email_key UNIQUE (email),
	CONSTRAINT tb_participante_pkey PRIMARY KEY (id),
	CONSTRAINT fk7dv5qpd5x6oda94yfcobkomdw FOREIGN KEY (evento_id) REFERENCES public.tb_evento(id)
    );
    CREATE TABLE public.tb_evento (
	data_fim date NOT NULL,
	data_inicio date NOT NULL,
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	nome varchar(255) NOT NULL,
	CONSTRAINT tb_evento_pkey PRIMARY KEY (id)
    );

# Tabela de API's 

| Método | Endpoint                                      | Descrição                                      |
|--------|-----------------------------------------------|------------------------------------------------|
| GET    | /api/evento                                   | get-evento                                     |
| POST   | /api/evento                                   | create-evento                                  |
| GET    | /api/evento/search/findByNome                 | executeSearch-evento-get                       |
| GET    | /api/evento/{id}                              | get-evento                                     |
| PUT    | /api/evento/{id}                              | update-evento                                  |
| DELETE | /api/evento/{id}                              | delete-evento                                  |
| PATCH  | /api/evento/{id}                              | patch-evento                                   |
| GET    | /api/participante                             | get-participante                               |
| POST   | /api/participante                             | create-participante                            |
| GET    | /api/participante/{id}                        | get-participante                               |
| PUT    | /api/participante/{id}                        | update-participante                            |
| DELETE | /api/participante/{id}                        | delete-participante                            |
| PATCH  | /api/participante/{id}                        | patch-participante                             |
| GET    | /api/participante/{id}/evento                 | get-evento-by-participante-Id                  |
| PUT    | /api/participante/{id}/evento                 | update-evento-by-participante-Id               |
| DELETE | /api/participante/{id}/evento                 | delete-evento-by-participante-Id               |
| PATCH  | /api/participante/{id}/evento                 | patch-evento-by-participante-Id                |
| GET    | /api/participante/{id}/evento/{propertyId}    | get-evento-by-participante-Id                  |
| DELETE | /api/participante/{id}/evento/{propertyId}    | delete-evento-by-participante-Id               |
| GET    | /api/presenca                                 | get-presenca                                   |
| GET    | /api/presenca/search/findAllByParticipante    | executeSearch-presenca-get                     |
| GET    | /api/presenca/{id}                            | get-presenca                                   |
| DELETE | /api/presenca/{id}                            | delete-presenca                                |
| GET    | /api/presenca/{id}/participante               | get-participante-by-presenca-Id                |
| DELETE | /api/presenca/{id}/participante               | delete-participante-by-presenca-Id             |
| GET    | /api/presenca/{id}/participante/{propertyId}  | get-participante-by-presenca-Id               |
| DELETE | /api/presenca/{id}/participante/{propertyId}  | delete-participante-by-presenca-Id            |
| GET    | /api/profile                                  | listAllFormsOfMetadata                         |
| GET    | /api/profile/evento                           | descriptor                                     |
| GET    | /api/profile/participante                     | descriptor_1                                   |
| GET    | /api/profile/presenca                         | descriptor_2                                   |
| PUT    | /api/presenca/atualiza/{id}                   | putPresenca                                   |
| POST   | /api/presenca/cadastro                        | postPresenca                                   |
| POST   | /api/auth/login                               | Autentica o usuário e retorna o JWT            |


## Como utilizar
  - Certifique-se que o seu docker local detém permissões a nível sudo

```bash
    git clone https://github.com/JoaolimaDev/desafio-mobsolution.git
    docker compose up --build
    cd jsf-app
    mvn spring-boot:run
```


1. **SWAGGER DISPONÍVEL**
   - **URL:** http://localhost:8080/swagger-ui/index.html

1. **Front-end**
   - **URL:**  http://localhost:4200/
  

**Usuário disponível para autênticação**
- ** email: user1**
- ** senha  user123**


<p align="left">
  💌 Contatos: ⤵️
</p>

<p align="left">
  <a href="mailto:ozymandiasphp@gmail.com" title="Gmail">
  <img src="https://img.shields.io/badge/-Gmail-FF0000?style=flat-square&labelColor=FF0000&logo=gmail&logoColor=white&link=LINK-DO-SEU-GMAIL" alt="Gmail"/></a>
  <a href="https://www.linkedin.com/in/jo%C3%A3o-vitor-de-lima-74441b1b1/" title="LinkedIn">
  <img src="https://img.shields.io/badge/-Linkedin-0e76a8?style=flat-square&logo=Linkedin&logoColor=white&link=LINK-DO-SEU-LINKEDIN" alt="LinkedIn"/></a>
  <a href="https://wa.me/5581989553431" title="WhatsApp">
  <img src="https://img.shields.io/badge/-WhatsApp-25d366?style=flat-square&labelColor=25d366&logo=whatsapp&logoColor=white&link=API-DO-SEU-WHATSAPP" alt="WhatsApp"/></a>
</p>
