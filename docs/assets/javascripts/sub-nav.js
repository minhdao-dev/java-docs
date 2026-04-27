(function () {
    'use strict';

    function getSectionText(item) {
        var child = item.querySelector(':scope > .md-nav');
        if (child) {
            var lbl = (child.getAttribute('aria-label') || '').trim();
            if (lbl) return lbl;
        }
        var ellipsis = item.querySelector(':scope > .md-nav__link .md-ellipsis, :scope > label .md-ellipsis');
        if (ellipsis) return ellipsis.textContent.trim();
        var header = item.querySelector(':scope > label.md-nav__link, :scope > .md-nav__link');
        if (header) {
            var clone = header.cloneNode(true);
            clone.querySelectorAll('svg, .md-nav__icon, span[class]').forEach(function (el) { el.remove(); });
            return clone.textContent.trim();
        }
        return '';
    }

    function findSectionItems(sidebar) {
        var all = Array.from(sidebar.querySelectorAll('.md-nav__item--section'));
        if (all.length === 0) return [];

        // Group by immediate parent — sections sharing a parent are siblings
        var parentMap = new Map();
        all.forEach(function (item) {
            var p = item.parentElement;
            if (!parentMap.has(p)) parentMap.set(p, []);
            parentMap.get(p).push(item);
        });

        // Return the largest sibling group (our language sections)
        var best = [];
        parentMap.forEach(function (group) {
            if (group.length > best.length) best = group;
        });
        return best;
    }

    function modifyTabs(sections, activeIdx) {
        var tabsList = document.querySelector('.md-tabs__list');
        if (!tabsList) return;

        tabsList.innerHTML = sections.map(function (sec, i) {
            var active = i === activeIdx;
            return '<li class="md-tabs__item' + (active ? ' md-tabs__item--active' : '') + '">' +
                   '<a class="md-tabs__link' + (active ? ' md-tabs__link--active' : '') + '" href="' +
                   sec.href + '">' + sec.text + '</a></li>';
        }).join('');
    }

    function filterSidebar(sectionItems, activeIdx) {
        sectionItems.forEach(function (item, i) {
            if (i === activeIdx) {
                item.classList.add('is-active');
                item.classList.remove('is-hidden');
            } else {
                item.classList.add('is-hidden');
                item.classList.remove('is-active');
            }
        });
    }

    function init() {
        document.body.classList.remove('has-subnav');

        var sidebar = document.querySelector('.md-sidebar--primary');
        if (!sidebar) return;

        var sectionItems = findSectionItems(sidebar);
        if (sectionItems.length < 2) return;   // Home page or no sections — leave tabs untouched

        var activeIdx = sectionItems.findIndex(function (item) {
            return !!item.querySelector('.md-nav__link--active');
        });
        if (activeIdx < 0) activeIdx = 0;

        var sections = sectionItems.map(function (item) {
            var text = getSectionText(item);
            var firstLink = item.querySelector('a.md-nav__link[href]');
            var href = firstLink ? firstLink.getAttribute('href') : '#';
            return { text: text, href: href };
        });

        modifyTabs(sections, activeIdx);
        filterSidebar(sectionItems, activeIdx);
        document.body.classList.add('has-subnav');
    }

    if (typeof document$ !== 'undefined') {
        document$.subscribe(init);
    } else {
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', init);
        } else {
            init();
        }
    }
})();
