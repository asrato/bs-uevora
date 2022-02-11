const onLoadClassification = () => {
    const form = document.getElementById("text");
    const abc = document.getElementById("abc");

    if (abc == null) {
        form.style.display = 'none';
    }
}

const onClickGeneral = () => {
    try {
        const general = document.getElementsByClassName("classification-g")[0];
        const female = document.getElementsByClassName("classification-f")[0];
        const male = document.getElementsByClassName("classification-m")[0];

        general.style.display = 'flex';
        female.style.display = 'none';
        male.style.display = 'none';
    } catch (err) {

    }
}
const onClickFemale = () => {
    try {
        const general = document.getElementsByClassName("classification-g")[0];
        const female = document.getElementsByClassName("classification-f")[0];
        const male = document.getElementsByClassName("classification-m")[0];

        general.style.display = 'none';
        female.style.display = 'flex';
        male.style.display = 'none';
    } catch (err) {

    }
}
const onClickMale = () => {
    try {
        const general = document.getElementsByClassName("classification-g")[0];
        const female = document.getElementsByClassName("classification-f")[0];
        const male = document.getElementsByClassName("classification-m")[0];

        general.style.display = 'none';
        female.style.display = 'none';
        male.style.display = 'flex';
    } catch (err) {

    }
}

const onClickSearchName = () => {
    const events = document.getElementById("events-to-display").childNodes;
    let count = 0;

    document.getElementsByName("event-date")[0].value = "";

    const searchTerm = document.getElementsByName("event-name")[0].value;

    for (const event of events) {
        const eventName = event.id.split("|")[0];

        if (eventName.toLowerCase().includes(searchTerm.toLowerCase())) {
            count++;
            event.style.display = 'flex';
        } else {
            event.style.display = 'none';
        }
    }

    if (count === 0) {
        document.getElementById("empty").style.display = 'flex';
    } else {
        document.getElementById("empty").style.display = 'none';
    }
}

const onClickSearchDate = () => {
    const events = document.getElementById("events-to-display").childNodes;
    let count = 0;

    document.getElementsByName("event-name")[0].value = "";

    const searchTerm = document.getElementsByName("event-date")[0].value;

    for (const event of events) {
        const eventDate = event.id.split("|")[1];

        if (eventDate === searchTerm || searchTerm === "") {
            count++;
            event.style.display = 'flex';
        } else {
            event.style.display = 'none';
        }
    }

    if (count === 0) {
        document.getElementById("empty").style.display = 'flex';
    } else {
        document.getElementById("empty").style.display = 'none';
    }
}

const reset = () => {

    const startdiv = document.getElementById("participantsStart");
    const start = document.getElementById("start");

    const p1div = document.getElementById("participantsP1");
    const p1 = document.getElementById("p1");

    const p2div = document.getElementById("participantsP2");
    const p2 = document.getElementById("p2");

    const p3div = document.getElementById("participantsP3");
    const p3 = document.getElementById("p3");

    const finishdiv = document.getElementById("participantsFinish");
    const finish = document.getElementById("finish");

    try {
        start.style.display = 'none';
    } catch (error) {

    }
    try {
        startdiv.style.display = 'none';
    } catch (error) {

    }

    try {
        p1.style.display = 'none';
    } catch (error) {

    }
    try {
        p1div.style.display = 'none';
    } catch (error) {

    }

    try {
        p2.style.display = 'none';
    } catch (error) {

    }
    try {
        p2div.style.display = 'none';
    } catch (error) {

    }

    try {
        p3.style.display = 'none';
    } catch (error) {

    }
    try {
        p3div.style.display = 'none';
    } catch (error) {

    }

    try {
        finish.style.display = 'none';
    } catch (error) {

    }
    try {
        finishdiv.style.display = 'none';
    } catch (error) {

    }
}

const onClickSection = (section) => {
    reset();

    try {
        switch (section) {
            case 'start':
                const startdiv = document.getElementById("participantsStart");
                const start = document.getElementById("start");

                try {
                    start.style.display = 'flex';
                } catch (e) {
                    startdiv.style.display = 'flex';
                }
                break;
            case 'p1':
                const p1div = document.getElementById("participantsP1");
                const p1 = document.getElementById("p1");

                try {
                    p1.style.display = 'flex';
                } catch (e) {
                    p1div.style.display = 'flex';
                }
                break;
            case 'p2':
                const p2div = document.getElementById("participantsP2");
                const p2 = document.getElementById("p2");

                try {
                    p2.style.display = 'flex';
                } catch (e) {
                    p2div.style.display = 'flex';
                }
                break;
            case 'p3':
                const p3div = document.getElementById("participantsP3");
                const p3 = document.getElementById("p3");

                try {
                    p3.style.display = 'flex';
                } catch (e) {
                    p3div.style.display = 'flex';
                }
                break;
            case 'finish':
                const finishdiv = document.getElementById("participantsFinish");
                const finish = document.getElementById("finish");

                try {
                    finish.style.display = 'flex';
                } catch (e) {
                    finishdiv.style.display = 'flex';
                }
                break;
        }
    } catch (error) {
        console.log('error')
    }
}