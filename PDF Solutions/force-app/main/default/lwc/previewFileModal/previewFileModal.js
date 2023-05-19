import { LightningElement, api } from "lwc";

export default class PreviewFileModal extends LightningElement {
  @api url;
  showFrame = false;
  showModal = false;
  @api show() {
   console.log('url ',this.url);
    this.showModal = true;
  }
  closeModal() {
    this.convertIt(false);
  }
  convertToPdf(){
    this.convertIt(true);
  }
  convertIt(performValue){
    this.dispatchEvent(
      new CustomEvent('convert', {
          bubbles: true,
          composed: true,
          detail: {
              performCallout: performValue,
          }
      })
  );
  this.showModal = false;

  }
}