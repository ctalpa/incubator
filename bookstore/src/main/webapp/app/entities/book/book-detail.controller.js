(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('BookDetailController', BookDetailController);

    BookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Book', 'Author'];

    function BookDetailController($scope, $rootScope, $stateParams, entity, Book, Author) {
        var vm = this;
        vm.book = entity;
        
        var unsubscribe = $rootScope.$on('bookstoreApp:bookUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
