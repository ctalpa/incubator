(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .factory('AuthorSearch', AuthorSearch);

    AuthorSearch.$inject = ['$resource'];

    function AuthorSearch($resource) {
        var resourceUrl =  'api/_search/authors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
