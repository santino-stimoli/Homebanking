const { createApp } = Vue
createApp({
    data() {
        return {
            // Cliente
            client: {},
            accounts: [],
            transactions: {},
            clientLoans: [],
            allTransactions: [],
            cards: [],
            debitCards: [],
            creditCards: [],
            loans: [],

            // Detalles
            numberActualAccount: "",
            balanceActualAccount:"",
            textarea1: "",
            messageContact: "",

            // Crear tarjetas
            color: "",
            type: "",
            silverCards: "",
            goldCards: "",
            titaniumCards: "",

            // Crear transacciones
            transfer: {
                mount: "",
                description: "",
                numberAccountOrigin: "",
                numberAccountDestiny: "",
                numberAccountDestinyExtern: "",
            },

            // Solicitar un préstamo
            loan: {
                id: "",
                amount: "",
                payments: 0,
                numberAccount: "",
                loanSelectedPayments: [],
            },

           

        }
    },
    created() {
        this.getClients()
    },

    methods: {

        getClients() {
            axios.get("/api/clients/current").then((Client) => {

                this.client = Client.data;

                this.accounts = [...this.client.accounts].sort((a, b) => a.id - b.id)

                this.clientLoans = [...this.client.loans].sort((a, b) => a.id - b.id)

                if (location.href.split(".html")[0].split("web/")[1] == "account") {

                    this.transactions = [...this.getObjectByIdLocation(this.accounts)[0].transactions].sort((a, b) => b.id - a.id)
                    this.numberActualAccount = this.getObjectByIdLocation(this.accounts)[0].number;
                    this.balanceActualAccount = this.getObjectByIdLocation(this.accounts)[0].balance;

                }

                const allTransactions = [];
                this.allTransactions = this.accounts.forEach(account => {
                    account.transactions.forEach(transaction =>{
                        allTransactions.push(transaction);
                    })
                });
                    
                this.allTransactions = [...allTransactions].sort((a, b) => b.id - a.id)

                this.cards = [...this.client.cards].sort((a, b) => a.id - b.id)
                this.countCardType()
                this.countCardColor()

            })
            axios.get("/api/loans").then((Loans) => {

                this.loans = Loans.data

            })
        },


        // Creaciones
        createAccount() {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                text: "Estas por crear una cuenta nueva en Pegasus",
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Aceptar',
                cancelButtonText: 'Cancelar',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/clients/current/accounts').then(response => this.getClients()).catch(response => {
                        const swalWithBootstrapButtons = Swal.mixin({
                            customClass: {
                                cancelButton: 'btn btn-danger'
                            },
                            buttonsStyling: false
                        })

                        swalWithBootstrapButtons.fire({
                            title: "Hemos detectado un error",
                            text: response.response.data,
                            icon: "error",
                            showConfirmButton: false,
                            showCancelButton: true,
                            cancelButtonText: 'Aceptar',
                        })
                    })
                }
            })
        },

        createCard() {
            const { value: cardType } = Swal.fire({
                title: 'Crea tu tarjeta de Pegasus',
                input: 'select',
                inputOptions: {
                    CREDIT: 'Credito',
                    DEBIT: 'Debito',
                },
                inputPlaceholder: 'Tipo de tarjeta',
                showCancelButton: true,
                confirmButtonText: 'Aceptar',
                cancelButtonText: 'Cancelar',
                inputValidator: (cardType) => {
                    return new Promise((resolve) => {
                        if (cardType === '') {
                            resolve('Tienes que seleccionar una de las opciones')
                        } else if (cardType === 'DEBIT' && this.debitCards.length == 3) {
                            resolve('Llegaste al límite de tarjetas de Débito creadas')
                        } else if (cardType === 'CREDIT' && this.creditCards.length == 3) {
                            resolve('Llegaste al límite de tarjetas de Crédito creadas')
                        } else {
                            const { value: color } = Swal.fire({
                                title: 'Crea tu tarjeta de Pegasus',
                                input: 'select',
                                inputOptions: {
                                    SILVER: 'Silver',
                                    GOLD: 'Gold',
                                    TITANIUM: 'Titanium',
                                },
                                inputPlaceholder: 'Categoria de tarjeta',
                                showCancelButton: true,
                                confirmButtonText: 'Aceptar',
                                cancelButtonText: 'Cancelar',
                                inputValidator: (color) => {
                                    return new Promise((resolve) => {
                                        if (color === '') {
                                            resolve('Tienes que seleccionar una de las opciones')
                                        }
                                        //else if (cardType == "CREDIT" && this.creditCards.length == 3) {
                                        //     resolve('Llegaste al límite de tarjetas ' + color.toLowerCase() + ' de crédito creadas')
                                        // } else if (cardType == "DEBIT" && this.debitCards.length == 3) {
                                        //     resolve('Llegaste al límite de tarjetas ' + color.toLowerCase() + ' de débito creadas')
                                        // }
                                        else {
                                            const swalWithBootstrapButtons = Swal.mixin({
                                                customClass: {
                                                    confirmButton: 'btn btn-success',
                                                    cancelButton: 'btn btn-danger'
                                                },
                                                buttonsStyling: false
                                            })

                                            swalWithBootstrapButtons.fire({
                                                title: '¿Estás seguro?',
                                                text: "Estas por crear una tarjeta nueva de " + cardType.toLowerCase() + "o con la categoria " + color.toLowerCase() + " en Pegasus",
                                                icon: 'question',
                                                showCancelButton: true,
                                                confirmButtonText: 'Aceptar',
                                                cancelButtonText: 'Cancelar',
                                                reverseButtons: true
                                            }).then((result) => {
                                                if (result.isConfirmed) {
                                                    axios.post('/api/clients/current/cards', "cardType=" + cardType + "&color=" + color, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => this.getClients(), console.log(cardType, color)).catch(response => {
                                                        const swalWithBootstrapButtons = Swal.mixin({
                                                            customClass: {
                                                                cancelButton: 'btn btn-danger'
                                                            },
                                                            buttonsStyling: false
                                                        })

                                                        swalWithBootstrapButtons.fire({
                                                            title: "Hemos detectado un error",
                                                            text: response.response.data,
                                                            icon: "error",
                                                            showConfirmButton: false,
                                                            showCancelButton: true,
                                                            cancelButtonText: 'Aceptar',
                                                        })

                                                    })
                                                }
                                            })
                                        }
                                    })
                                }
                            })
                        }
                    })
                }
            })
        },

        // oficialCreateCard() {
        //     if (this.type != "" && this.color != "") {
        //         const swalWithBootstrapButtons = Swal.mixin({
        //             customClass: {
        //                 confirmButton: 'btn btn-success',
        //                 cancelButton: 'btn btn-danger'
        //             },
        //             buttonsStyling: false
        //         })

        //         swalWithBootstrapButtons.fire({
        //             title: '¿Estás seguro?',
        //             text: "Estas por crear una tarjeta nueva de " + this.type.toLowerCase() + "o con la categoria " + this.color.toLowerCase() + " en Pegasus",
        //             icon: 'question',
        //             showCancelButton: true,
        //             confirmButtonText: 'Aceptar',
        //             cancelButtonText: 'Cancelar',
        //             reverseButtons: true
        //         }).then((result) => {
        //             if (result.isConfirmed) {
        //                 axios.post('/api/clients/current/cards', "cardType=" + this.type + "&color=" + this.color, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => location.href = "/web/cards.html").catch(response => {
        //                     const swalWithBootstrapButtons = Swal.mixin({
        //                         customClass: {
        //                             cancelButton: 'btn btn-danger'
        //                         },
        //                         buttonsStyling: false
        //                     })

        //                     swalWithBootstrapButtons.fire({
        //                         title: "Hemos detectado un error",
        //                         text: response.response.data,
        //                         icon: "error",
        //                         showConfirmButton: false,
        //                         showCancelButton: true,
        //                         cancelButtonText: 'Aceptar',
        //                     })
        //                 })
        //             }
        //         })
        //     } else if (this.color == "" || this.type == "") {
        //         Swal.fire({
        //             title: 'Se ha detectado un error',
        //             text: "Los datos ingresados en el formulario son incompletos, revisa de haber completado todos los campos requeridos",
        //             icon: 'error',
        //             showCancelButton: false,
        //             confirmButtonText: 'Aceptar',
        //         })
        //     }
        // },

        doTransaction() {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })
            let mount = this.transfer.mount;
            let numberAccountOrigin = this.transfer.numberAccountOrigin;
            let numberAccountDestiny = "";
            if (document.getElementById("select-intern").classList.contains("d-none")) {
                numberAccountDestiny = "VIN" + this.transfer.numberAccountDestinyExtern;
            } else {
                numberAccountDestiny = this.transfer.numberAccountDestiny;
            }
            let description = this.transfer.description;

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                text: `Estas por enviar $${mount} a la cuenta ${numberAccountDestiny} desde tu cuenta ${numberAccountOrigin}`,
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Aceptar',
                cancelButtonText: 'Cancelar',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/transactions', "mount=" + mount + "&description=" + description + "&numberAccountOrigin=" + numberAccountOrigin + "&numberAccountDestiny=" + numberAccountDestiny, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(() => {
                            this.transfer.mount = ""
                            this.transfer.numberAccountOrigin = ""
                            this.transfer.numberAccountDestiny = ""
                            this.transfer.numberAccountDestinyExtern = ""
                            this.transfer.description = ""
                            swalWithBootstrapButtons.fire({
                                title: '¡Bien hecho!',
                                text: `Acabas de enviar $${mount} desde tu cuenta ${numberAccountOrigin} a la cuenta ${numberAccountDestiny}`,
                                icon: 'success',
                                showCancelButton: true,
                                confirmButtonText: 'Aceptar',
                                cancelButtonText: 'Cancelar',
                                reverseButtons: true
                            })
                        })
                }
            })
        },

        doLoan() {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                text: "Estas por crear un nuevo préstamo " + this.loanSelectedName + " de $" + (this.loan.amount * 1.2).toLocaleString() + " a pagar en " + this.loan.payments + " cuotas de $" + (this.loan.amount * 1.2 / this.loan.payments).toLocaleString(),
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Aceptar',
                cancelButtonText: 'Cancelar',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/loans', {
                        "id": this.loan.id,
                        "amount": this.loan.amount,
                        "payments": this.loan.payments,
                        "numberAccount": this.loan.numberAccount
                    }, { headers: { 'content-type': 'application/json' } })
                        .then(response => this.getClients()).then(response => {
                            const swalWithBootstrapButtons = Swal.mixin({
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                },
                                buttonsStyling: false
                            })

                            swalWithBootstrapButtons.fire({
                                title: "Todo salió a la perfección",
                                text: "Hemos concretado el préstamo, ya tienes el dinero depositado en tu cuenta " + this.loan.numberAccount,
                                icon: "success",
                                showConfirmButton: true,
                                showCancelButton: false,
                            })
                        }).catch(response => {
                            const swalWithBootstrapButtons = Swal.mixin({
                                customClass: {
                                    cancelButton: 'btn btn-danger'
                                },
                                buttonsStyling: false
                            })

                            swalWithBootstrapButtons.fire({
                                title: "Hemos detectado un error",
                                text: response.response.data,
                                icon: "error",
                                showConfirmButton: false,
                                showCancelButton: true,
                                cancelButtonText: 'Aceptar',
                            })
                        })
                }
            })
        },


        // Diseño funcional
        moveSidebar() {
            let button = document.getElementById("open-button").classList
            let sidebar = document.getElementById("sidebar-chico").classList
            if (!button.contains("esconder")) {
                button.add("esconder")
                sidebar.remove("esconder")

            } else {
                button.remove("esconder")
                sidebar.add("esconder")

            }
        },

        sendMessageContact() {
            Swal.fire({
                title: "Mensaje enviado!",
                text: "Gracias por dejar su mensaje y ayudarnos a mejorar la pagina, lo revisaremos lo mas pronto posible.",
                icon: "success",
                button: "Cerrar",
            }).then(response => this.messageContact = "")
        },

        // firstTime() {
        //     if (this.accounts.length == 0) {
        //         axios.post('/api/clients/current/accounts')
        //         const swalWithBootstrapButtons = Swal.mixin({
        //             customClass: {
        //                 confirmButton: 'btn btn-success',
        //             },
        //             buttonsStyling: false
        //         })

        //         swalWithBootstrapButtons.fire({
        //             title: 'Bienvenido ' + this.client.name.split(" ")[0],
        //             text: "¡Gracias por haberte sumado a Pegasus y confiar en nosotros!",
        //             icon: "info",
        //             confirmButtonText: 'Iniciar',
        //             reverseButtons: true
        //         })
        //         this.getClients()
        //     }

        // },

        changeInternExtern() {
            let selectIntern = document.getElementById("select-intern");
            let inputExtern = document.getElementById("input-extern");
            let titleIntern = document.getElementById("title-intern").classList;
            let titleExtern = document.getElementById("title-extern").classList;
            if (selectIntern.classList.contains("d-none")) {
                selectIntern.classList.remove("d-none")
                selectIntern.removeAttribute("required")
                inputExtern.classList.add("d-none")
                selectIntern.setAttribute("required", true)
                titleIntern.remove("d-none")
                titleExtern.add("d-none")
            } else {
                selectIntern.classList.add("d-none")
                selectIntern.setAttribute("required", true)
                inputExtern.classList.remove("d-none")
                selectIntern.removeAttribute("required")
                titleIntern.add("d-none")
                titleExtern.remove("d-none")
            }
        },

        logOut() {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                text: "Vas a cerrar tu sesión de Pegasus",
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Aceptar',
                cancelButtonText: 'Cancelar',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout').then(response => location.href = "http://localhost:8080")
                }
            })
        },


        // Herramientas
        getObjectByIdLocation(array) {
            let idLocation = new URL(window.location).searchParams.get("id");
            return transactionAccount = array.filter(x => x.id == idLocation);
        },

        countCardType() {
            this.debitCards = this.cards.filter(card => card.type == "DEBIT")
            this.creditCards = this.cards.filter(card => card.type == "CREDIT")
        },

        countCardColor() {
            this.silverCards = this.cards.filter(card => card.color == "SILVER")
            this.goldCards = this.cards.filter(card => card.color == "GOLD")
            this.titaniumCards = this.cards.filter(card => card.color == "TITANIUM")
        },

        datesTransactions(dateTime){
            
            let date = dateTime.split("T")[0]
            let hour = dateTime.split("T")[1].split(":")[0] + ":" + dateTime.split("T")[1].split(":")[1]

            let monthNow = new Date().getMonth() + 1 + "";
            let dayNow = new Date().getDate();
            
            if (Array.from(monthNow).length == 1) {
                monthNow = "0" + monthNow
            }

            if (!Array.from(dayNow).length == 1) {
                dayNow = "0" + dayNow
            }

            let dateNow =  new Date().getFullYear() + "-" + monthNow + "-" + dayNow;
            
            if (dateNow == date) {
                return hour
            } else {
                return date + " " + hour
            }
            
            // console.log(dateTime,date);

        }


    },
    computed: {

        getPayments() {
            if (this.loan.id != "") {
                let loanSelectedPayments = this.loans.filter(loan => loan.id == this.loan.id)
                this.loan.loanSelectedPayments = loanSelectedPayments[0].payments
                this.loan.loanSelectedName = loanSelectedPayments[0].name
            }
        }

    },
}
).mount('#app')

jQuery(function ($) {
    $(".sidebar-dropdown > a").click(function () {
        $(".sidebar-submenu").slideUp(200);
        if (
            $(this)
                .parent()
                .hasClass("active")
        ) {
            $(".sidebar-dropdown").removeClass("active");
            $(this)
                .parent()
                .removeClass("active");
        } else {
            $(".sidebar-dropdown").removeClass("active");
            $(this)
                .next(".sidebar-submenu")
                .slideDown(200);
            $(this)
                .parent()
                .addClass("active");
        }
    });
});

window.onload = function () {
    $("#onload").fadeOut();
    $("body").removeClass("hidden")
}