const { createApp } = Vue
createApp({
    data() {
        return {
            clients:{},
            register: {
                name: "",
                lastName: "",
                sex: "",
                age: "",
                email: "",
                password: "",
                passwordConfirm: "",
            },
            today: new Date().getTime(),
        }
    },
    created() {
        this.getClients()
    },
    methods: {

        getClients(){
            axios.get("/api/clients").then(response =>{

                this.clients = response.data;

            })
        },

        getAge() {
            let birthdate = new Date(this.register.age).getTime();
            let milisegundosDia = 1000 * 60 * 60 * 24;
            let diferenceBirthdayToday = this.today - birthdate;
            let diferenceYears = diferenceBirthdayToday / milisegundosDia / 365.25;
            return this.register.age = Math.floor(diferenceYears)
        },

        createClient() {
            axios.post('/api/clients', "name=" + this.register.name + "&lastName=" + this.register.lastName + "&sex=" + this.register.sex + "&age=" + this.getAge() + "&email=" + this.register.email + "&password=" + this.register.password + "&passwordConfirm=" + this.register.passwordConfirm, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    const swalWithBootstrapButtons = Swal.mixin({
                        customClass: {
                            confirmButton: 'btn btn-success',
                            cancelButton: 'btn btn-danger'
                        },
                        buttonsStyling: false
                    })

                    swalWithBootstrapButtons.fire({
                        title: "Usuario registrado!",
                        text: "Tu usuario ha sido registrado con exito, inicia sesión en Pegasus",
                        icon: "success",
                        button: "Aceptar",
                        confirmButtonText: 'Aceptar',
                        reverseButtons: true
                    })
                }).then(response => this.getClients()).catch(error => {
                    console.log(error);
                    const swalWithBootstrapButtons = Swal.mixin({
                        customClass: {
                            cancelButton: 'btn btn-danger'
                        },
                        buttonsStyling: false
                    })

                    swalWithBootstrapButtons.fire({
                        title: "Hemos detectado un error",
                        text: error.response.data,
                        icon: "error",
                        showConfirmButton: false,
                        showCancelButton: true,
                        cancelButtonText: 'Aceptar',
                    })
                })

        },

    },
    computed: {},
}
).mount('#app')

window.onload = function () {
    $("#onload").fadeOut();
    $("body").removeClass("hidden")
}