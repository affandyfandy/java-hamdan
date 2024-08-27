import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.component.html',
})
export class ProductFormComponent implements OnInit {
  @Input() product: Product | null = null;
  @Output() save = new EventEmitter<Product>();
  @Output() cancel = new EventEmitter<void>();

  productForm: FormGroup;

  isVisible = true;

  constructor(private fb: FormBuilder) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
      status: ['ACTIVE', Validators.required],
    });
  }

  ngOnInit(): void {
    if (this.product) {
      this.productForm.patchValue({
        name: this.product.name,
        price: this.product.price,
        status: this.product.status,
      });
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const productData = this.productForm.value;
      const currentDate = new Date().toISOString();

      if (this.product) {
        productData.id = this.product.id;
        productData.createdAt = this.product.createdAt;
        productData.updatedAt = currentDate;
      } else {
        productData.createdAt = currentDate;
        productData.updatedAt = currentDate;
      }

      this.save.emit(productData);
      this.onClose();
    }
  }

  onClose(): void {
    this.isVisible = false;
    this.cancel.emit();
  }
}
