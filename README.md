## GitHub Activity CLI

CLI que utiliza la API pública de GitHub para observar la actividad de los usuarios durante los pasados 90 días.

App basada en un reto propuesto por la plataforma [roadmap.sh](https://roadmap.sh/projects/github-user-activity).

## Funcionamiento

- Ingresa el nombre de usuario que te interese buscar.
- Ejemplo: `sOliveto93`

## Instalacion 

- Clonar el proyecto github
- java instalado
- jdk 17
- importar el proyecto a tu ide favorito.
- Run File en la clase Main
- Colocar en consola el perfil de github a buscar.

### Respuesta de la API:

```json
[
    {
        "id": "42480671640",
        "type": "CreateEvent",
        "actor": {
            "id": 173174425,
            "login": "sOliveto93",
            "display_login": "sOliveto93",
            "gravatar_id": "",
            "url": "https://api.github.com/users/sOliveto93",
            "avatar_url": "https://avatars.githubusercontent.com/u/173174425?"
        },
        "repo": {
            "id": 866649623,
            "name": "sOliveto93/pruebasTecnicas",
            "url": "https://api.github.com/repos/sOliveto93/pruebasTecnicas"
        },
        "payload": {
            "ref": null,
            "ref_type": "repository",
            "master_branch": "main",
            "description": "repo para distintas pruebas técnicas",
            "pusher_type": "user"
        },
        "public": true,
        "created_at": "2024-10-02T16:25:04Z"
    }
]





