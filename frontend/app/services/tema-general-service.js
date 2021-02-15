import Ember from 'ember';
import EmberizedResourceCreatorInjected from "ateam-ember-resource/mixins/emberized-resource-creator-injected";

export default Ember.Service.extend(EmberizedResourceCreatorInjected, {

  getAllTemasGenerales:function(){
    return this._temaGeneralResource().getAll();
  },

  createTemaGeneral:function(tema){
    return this._temaGeneralResource().create(tema);
  },

  deleteTemaGeneral:function(tema){
    return this._temaGeneralResource().remove(tema);
  },

  updateTemaGeneral:function(tema) {
    return this._temaGeneralResource().update(tema);
  },

  // PRIVATE
  _temaGeneralResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('temas-generales');
    return resource;
  },

});
