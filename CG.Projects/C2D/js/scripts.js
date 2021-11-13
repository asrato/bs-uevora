let canvas = document.querySelector("#canvas");
canvas.width = 500;
canvas.height = 500;
let ctx = canvas.getContext('2d');

let gradient = function () {
    let gr = ctx.createLinearGradient(0, 0, 0, 180);
    gr.addColorStop(0.0, "#737373");
    gr.addColorStop(0.45, "#737373");
    gr.addColorStop(0.55, "#1a1a1a");
    gr.addColorStop(1.0, "#1a1a1a");

    return gr;
}

let left_siren = {
    points: [
        { x: 12, y: 12, rectangle: null }
    ],
    aspect: { fill: '#e60000' },
    transform: { x: 118, y: 21, sx: 1, sy: 1, a: 0 }
};

let right_siren = {
    points: [
        { x: 12, y: 12, rectangle: null }
    ],
    aspect: { fill: '#0033cc' },
    transform: { x: 141, y: 21, sx: 1, sy: 1, a: 0 }
};

let grey_square = {
    points: [
        { x: 12, y: 12, rectangle: null }
    ],
    aspect: { fill: '#e6e6e6' },
    transform: { x: 129, y: 21, sx: 1, sy: 1, a: 0 }
};

let car_top = {
    points: [
        { x: 20, y: 80, move: null },
        { x: 20, y: 78, line: null },
        { cx: 30, cy: 65, x: 65, y: 60, quadratic: null },
        { cx: 140, cy: 0, x: 200, y: 60, quadratic: null },
        { x: 215, y: 60, line: null },
        { cx: 220, cy: 60, x: 220, y: 65, quadratic: null },
        { x: 220, y: 80, line: null }
    ],
    aspect: { fill: '#e6e6e6' },
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 }
};

let car_bottom = {
    points: [
        { x: 20, y: 80, move: null },
        { x: 20, y: 90, line: null },
        { cx: 20, cy: 105, x: 35, y: 105, quadratic: null },
        { x: 210, y: 105, line: null },
        { cx: 220, cy: 105, x: 220, y: 95, quadratic: null },
        { x: 220, y: 80, line: null }
    ],
    aspect: { fill: gradient() },
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 }
};

let rim = {
    points: [
        { radius: 12, circle: null }
    ],
    aspect: { fill: '#bfbfbf' },
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 }
};

let tire = {
    points: [
        { radius: 20, circle: null }
    ],
    aspect: { fill: '#1a1a1a' },
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 }
};

let front_wheel = {
    transform: { x: 60, y: 105, sx: 1, sy: 1, a: 0 },
    children: [
        tire,
        rim
    ]
};

let back_wheel = {
    transform: { x: 180, y: 105, sx: 1, sy: 1, a: 0 },
    children: [
        tire,
        rim
    ]
};

let front_light = {
    points: [
        { x: 20, y: 10, rectangle: null }
    ],
    aspect: { fill: '#ffff00' },
    transform: { x: 20, y: 78, sx: 1, sy: 1, a: 0 }
};

let back_light = {
    points: [
        { x: 8, y: 20, rectangle: null }
    ],
    aspect: { fill: '#e60000' },
    transform: { x: 212, y: 70, sx: 1, sy: 1, a: 0 }
};

let front_window = {
    points: [
        { x: 10, y: 60, move: null },
        { cx: 55, cy: 20, x: 100, y: 20, quadratic: null },
        { x: 100, y: 60, line: null },
        { x: 10, y: 60, line: null }
    ],
    aspect: { fill: '#52667a' },
    transform: { x: 71, y: 22, sx: 0.6, sy: 0.65, a: 0 }
};

let back_window = {
    points: [
        { x: 10, y: 20, move: null },
        { cx: 60, cy: 20, x: 95, y: 60, quadratic: null },
        { x: 10, y: 60, line: null },
        { x: 10, y: 20, line: null }
    ],
    aspect: { fill: '#52667a' },
    transform: { x: 131, y: 22, sx: 0.6, sy: 0.65, a: 0 }
};

let car = {
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 },
    children: [
        left_siren,
        right_siren,
        grey_square,
        car_top,
        car_bottom,
        front_wheel,
        back_wheel,
        front_light,
        back_light,
        front_window,
        back_window
    ]
};

let sky = {
    points: [
        { x: 500, y: 500, rectangle: null }
    ],
    aspect: { fill: '#ccebff' },
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 }
};

let road = {
    points: [
        { x: 800, y: 100, rectangle: null }
    ],
    aspect: { fill: '#262626' },
    transform: { x: 0, y: 400, sx: 1, sy: 1, a: 0 }
};

let sun = {
    points: [
        { radius: 35, circle: null }
    ],
    aspect: { fill: '#ffff66' },
    transform: { x: 140, y: 60, sx: 1, sy: 1, a: 0 }
};

let top_fence = {
    points: [
        { x: 500, y: 10, rectangle: null }
    ],
    aspect: { fill: "#9E9696" },
    transform: { x: 0, y: 380, sx: 1, sy: 1, a: 0 }
};

let bottom_fence = {
    points: [
        { x: 500, y: 10, rectangle: null }
    ],
    aspect: { fill: "#9E9696" },
    transform: { x: 0, y: 355, sx: 1, sy: 1, a: 0 }
};

let middle_fence = {
    points: [
        { x: 500, y: 15, rectangle: null }
    ],
    aspect: { fill: "#B3AFAF" },
    transform: { x: 0, y: 365, sx: 1, sy: 1, a: 0 }
};

let vertical_bar1 = {
    points: [
        { x: 20, y: 55, rectangle: null }
    ],
    aspect: { fill: "#B3AFAF" },
    transform: { x: 15, y: 345, sx: 1, sy: 1, a: 0 }
};

let vertical_bar2 = {
    points: [
        { x: 20, y: 55, rectangle: null }
    ],
    aspect: { fill: "#B3AFAF" },
    transform: { x: 109, y: 345, sx: 1, sy: 1, a: 0 }
};

let vertical_bar3 = {
    points: [
        { x: 20, y: 55, rectangle: null }
    ],
    aspect: { fill: "#B3AFAF" },
    transform: { x: 203, y: 345, sx: 1, sy: 1, a: 0 }
};

let vertical_bar4 = {
    points: [
        { x: 20, y: 55, rectangle: null }
    ],
    aspect: { fill: "#B3AFAF" },
    transform: { x: 297, y: 345, sx: 1, sy: 1, a: 0 }
};

let vertical_bar5 = {
    points: [
        { x: 20, y: 55, rectangle: null }
    ],
    aspect: { fill: "#B3AFAF" },
    transform: { x: 391, y: 345, sx: 1, sy: 1, a: 0 }
};

let vertical_bar6 = {
    points: [
        { x: 20, y: 55, rectangle: null }
    ],
    aspect: { fill: "#B3AFAF" },
    transform: { x: 470, y: 345, sx: 1, sy: 1, a: 0 }
};

let fence = {
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 },
    children: [
        vertical_bar1,
        vertical_bar2,
        vertical_bar3,
        vertical_bar4,
        vertical_bar5,
        vertical_bar6,
        middle_fence,
        bottom_fence,
        top_fence
    ]
};

let background = {
    transform: { x: 0, y: 0, sx: 1, sy: 1, a: 0 },
    children: [
        sky,
        fence,
        sun,
        road
    ]
};

let airplane = {
    points: [
        { x: 5, y: 42, move: null },
        { x: 5, y: 35, line: null },
        { x: 25, y: 20, line: null },
        { x: 25, y: 15, line: null },
        { cx: 25, cy: 6, x: 30, y: 6, quadratic: null },
        { cx: 35, cy: 6, x: 35, y: 15, quadratic: null },
        { x: 35, y: 20, line: null },
        { x: 55, y: 35, line: null },
        { x: 55, y: 42, line: null },
        { x: 35, y: 32, line: null },
        { x: 35, y: 46, line: null },
        { x: 40, y: 50, line: null },
        { x: 40, y: 57, line: null },
        { x: 30, y: 52, line: null },
        { x: 20, y: 57, line: null },
        { x: 20, y: 50, line: null },
        { x: 25, y: 46, line: null },
        { x: 25, y: 32, line: null }
    ],
    aspect: { fill: '#404040' },
    transform: { x: 0, y: 0, sx: -1, sy: -1, a: 90 },
};

let airplane_model = {
    plane: airplane,
    fly: true,
    initial_position: { x: 520, y: 120 }
};

let police_car = {
    car: car,
    left: true,
    initial_position: { x: 500, y: 295 }
};

let draw = function (figure) {
    let t = figure.transform;

    ctx.save();
    ctx.translate(t.x, t.y);
    ctx.scale(t.sx, t.sy);
    ctx.rotate(t.a);

    if (figure.hasOwnProperty('children')) {
        for (let child of figure.children) {
            draw(child);
        }
    }
    if (figure.hasOwnProperty('points'))
        draw_shape(figure.points);

    if (figure.hasOwnProperty('aspect')) {
        let a = figure.aspect;
        if (a.hasOwnProperty('fill')) {
            ctx.fillStyle = a.fill;
            ctx.fill();
        }
        if (a.hasOwnProperty('stroke')) {
            ctx.strokeStyle = a.stroke;
            ctx.stroke();
        }
    }

    ctx.restore();
};

let draw_shape = function (points) {
    ctx.beginPath();
    for (let point of points) {
        if (point.hasOwnProperty('rectangle')) {
            ctx.lineTo(point.x, 0);
            ctx.lineTo(point.x, point.y);
            ctx.lineTo(0, point.y);
            ctx.lineTo(0, 0);
        }
        if (point.hasOwnProperty('move'))
            ctx.moveTo(point.x, point.y);
        if (point.hasOwnProperty('line'))
            ctx.lineTo(point.x, point.y);
        if (point.hasOwnProperty('quadratic'))
            ctx.quadraticCurveTo(point.cx, point.cy, point.x, point.y);
        if (point.hasOwnProperty('circle'))
            ctx.arc(0, 0, point.radius, 0, 2 * Math.PI);
        if (point.hasOwnProperty('bezier'))
            ctx.bezierCurveTo(point.cx1, point.cy1, point.cx2, point.cy2, point.x, point.i);
    }
    ctx.closePath();
};

let draw_dash = function () {
    ctx.save();
    ctx.translate(0, 435);
    ctx.fillStyle = '#e6e6e6';
    let x = 3;
    for (let i = 0; i < 8; i++) {
        ctx.fillRect(x, 0, 40, 12);
        x += 65;
    }
    ctx.restore();
}

let draw_cloud = function () {
    ctx.save();
    ctx.translate(-110, 50);
    ctx.scale(1, 0.6);
    ctx.beginPath();
    ctx.moveTo(170, 80);
    ctx.bezierCurveTo(130, 100, 130, 150, 230, 150);
    ctx.bezierCurveTo(250, 180, 320, 180, 340, 150);
    ctx.bezierCurveTo(420, 150, 420, 120, 390, 100);
    ctx.bezierCurveTo(430, 40, 370, 30, 340, 50);
    ctx.bezierCurveTo(320, 5, 250, 20, 250, 50);
    ctx.bezierCurveTo(200, 5, 150, 20, 170, 80);
    ctx.closePath();
    ctx.fillStyle = '#f2f2f2';
    ctx.fill();
    ctx.restore();
}

let draw_police_car = function (scene) {
    ctx.save();
    ctx.translate(police_car.initial_position.x, police_car.initial_position.y);
    draw(police_car.car);
    ctx.restore();
};

let draw_airplane = function (plane) {
    ctx.save();
    ctx.translate(plane.initial_position.x, plane.initial_position.y);
    draw(plane.plane);
    ctx.restore();
};

let x = function () {
    if (police_car.left) {
        if (police_car.initial_position.x > -300) {
            police_car.initial_position.x -= 4;
            airplane_model.initial_position.x -= 2;
            airplane_model.initial_position.y -= 1;
            if(police_car.initial_position.x % 100 == 0 && police_car.initial_position.x % 200 != 0) {
                left_siren.aspect.fill = '#0033cc';
                right_siren.aspect.fill = '#e60000';
            } else if (police_car.initial_position.x % 100 == 0) {
                left_siren.aspect.fill = '#e60000';
                right_siren.aspect.fill = '#0033cc';
            }
        } else {
            police_car.left = false;
        }
    }

    draw(background);
    draw_cloud();
    draw_dash();
    draw_police_car(police_car);
    draw_airplane(airplane_model);


    requestAnimationFrame(x);
};

requestAnimationFrame(x);