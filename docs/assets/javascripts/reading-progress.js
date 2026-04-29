document$.subscribe(function () {
    var bar = document.getElementById('reading-progress');
    if (!bar) {
        bar = document.createElement('div');
        bar.id = 'reading-progress';
        document.body.appendChild(bar);
    }
    bar.style.width = '0%';

    function update() {
        var scrolled = window.scrollY || document.documentElement.scrollTop;
        var total = document.documentElement.scrollHeight - window.innerHeight;
        bar.style.width = (total > 0 ? Math.min((scrolled / total) * 100, 100) : 0) + '%';
    }

    window.addEventListener('scroll', update, { passive: true });
    update();
});
