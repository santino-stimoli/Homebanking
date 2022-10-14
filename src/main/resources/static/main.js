const { createApp } = Vue
createApp({
    data() {
        return {
            logIn: {
                email: "",
                password: "",
            },
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
        this.clientUnauthenticated()
    },
    methods: {

        turnLogInRegister() {
            let loginRegister = document.getElementsByClassName("card-3d-wrapper")[0].classList;
            if (loginRegister.length == 1) {
                loginRegister.add("giro")
            } else {
                loginRegister.remove("giro")
            }
        },

        clientUnauthenticated() {
            axios.get("/api/clients/current").then(x => location.href = "/web/home.html").catch(() => 0)
        },

        getAge() {
            let birthdate = new Date(this.register.age).getTime();
            let milisegundosDia = 1000 * 60 * 60 * 24;
            let diferenceBirthdayToday = this.today - birthdate;
            let diferenceYears = diferenceBirthdayToday / milisegundosDia / 365.25;
            return this.register.age = Math.floor(diferenceYears)
        },

        LogIn() {
            if (this.logIn.email.includes("@admin")) {
                axios.post('/api/login', "email=" + this.logIn.email + "&password=" + this.logIn.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => location.href = "manager/manager.html").catch(error => {
                    Swal.fire({
                        title: "Hemos detectado un error",
                        text: "Revisa que los datos colocados en el formulario sean correctos",
                        icon: "error",
                        showConfirmButton: false,
                        showCancelButton: true,
                        cancelButtonText: 'Aceptar',
                    })
                })
            } else {
                axios.post('/api/login', "email=" + this.logIn.email + "&password=" + this.logIn.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => location.href = "web/home.html").catch(error => {
                    Swal.fire({
                        title: "Hemos detectado un error",
                        text: "Revisa que los datos colocados en el formulario sean correctos",
                        icon: "error",
                        showConfirmButton: false,
                        showCancelButton: true,
                        cancelButtonText: 'Aceptar',
                    })
                })
            }
        },

        Register() {
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
                    }).then(result => location.href = "")
                }).catch(error => {
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

        seePassword(passwords, ojos) {
            let password = document.getElementById(passwords);
            let ojo = document.getElementById(ojos);
            if (password.getAttribute("type") == "password") {
                password.setAttribute('type', 'text')
                ojo.classList.add("password-type-text")
            } else {
                password.setAttribute('type', 'password')
                ojo.classList.remove("password-type-text")
            }
        },

    },
    computed: {},
}
).mount('#app')

window.onload = function () {
    $("#onload").fadeOut();
    $("body").removeClass("hidden")
}