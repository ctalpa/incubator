(function() {
    'use strict';

    angular
        .module('rxmenApp')
        .factory('Sessions', Sessions);

    Sessions.$inject = ['$resource'];

    function Sessions ($resource) {
        return $resource('api/account/sessions/:series', {}, {
            'getAll': { method: 'GET', isArray: true}
        });
    }
})();
